package com.iceland.betradar.listeners;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iceland.betradar.model.ProtocolConstants;
import com.iceland.betradar.model.iceland.oddsentity.IcelandOddsEntity;
import com.iceland.betradar.service.BetRadarState;
import com.iceland.betradar.service.GroupNameOrderMapper;
import com.iceland.betradar.service.persist.lcoo.LcooPersist;
import com.iceland.betradar.service.persist.live.LiveBetResultsNormalPersist;
import com.iceland.betradar.service.persist.live.LiveBetResultsRollBackPersist;
import com.iceland.betradar.service.DataContainer;
import com.iceland.betradar.service.IcelandSocket;
import com.iceland.betradar.service.JSON;
import com.sportradar.sdk.common.enums.FeedEventType;
import com.sportradar.sdk.feed.liveodds.classes.EventDataPackage;
import com.sportradar.sdk.feed.liveodds.entities.common.BetCancelEntity;
import com.sportradar.sdk.feed.liveodds.entities.common.BetCancelUndoEntity;
import com.sportradar.sdk.feed.liveodds.entities.common.BetClearEntity;
import com.sportradar.sdk.feed.liveodds.entities.common.BetClearRollbackEntity;
import com.sportradar.sdk.feed.liveodds.entities.common.BetStartEntity;
import com.sportradar.sdk.feed.liveodds.entities.common.BetStopEntity;
import com.sportradar.sdk.feed.liveodds.entities.common.IrrelevantOddsChangeEntity;
import com.sportradar.sdk.feed.liveodds.entities.common.MetaInfoEntity;
import com.sportradar.sdk.feed.liveodds.entities.common.OddsChangeEntity;
import com.sportradar.sdk.feed.liveodds.entities.common.OddsEntity;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.LiveOddsMetaData;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.MatchHeaderInfoEntity;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.MatchInfoEntity;
import com.sportradar.sdk.feed.liveodds.enums.OddsType;
import com.sportradar.sdk.feed.liveodds.interfaces.LiveOddsBasedFeed;
import com.sportradar.sdk.feed.liveodds.interfaces.LiveOddsBasedFeedListener;
import com.sportradar.sdk.feed.liveodds.interfaces.LiveOddsTestManager;

abstract class LiveOddsMatchBaseListener<T extends LiveOddsBasedFeed> implements LiveOddsBasedFeedListener<T> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Timer onceHourEventRequesterTimer = new Timer();
	private Timer oncePerDayEventRequesterTimer = new Timer();

	private Integer calculateOddValue(OddsEntity odd) {
		Integer value;
		switch (odd.getType()) {
			case ITEM3W:
				value = 100000;
				break;
			case FT3W:
				value = 10000;
				break;
			case FT2W:
				value = 1000;
				break;
			default:
				if(odd.getSubType() != null && LcooPersist.INSTANCE.getGroupNameByOddsType(odd.getSubType().intValue()).equals("popular_market")){
					value = 100;
				} else {
					value = 10;
				}
				break;
		}
		return value;
	}

	@Override
	public void onClosed(T sender) {
		logger.info("On closed");
		BetRadarState.INSTANCE.feedOpened.set(false);
		IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.BET_RADAR_CLIENT_DISCONNECTED, "critical");
	}

	@Override
	public void onFeedEvent(T sender, FeedEventType feedEventType) {
		logger.info("Feed event occurred. Event: {}", feedEventType);
	}

	@Override
	public void onOpened(T sender) {
		logger.info("On opened");
	}

	@Override
	public void onInitialized(final T sender) {
		logger.info("On initialized");
		BetRadarState.INSTANCE.feedOpened.set(true);
		DataContainer.INSTANCE.clearDataContainer();

		new Thread(() -> {
			if(sender.getTestManager() instanceof LiveOddsTestManager){
				((LiveOddsTestManager) sender.getTestManager()).startAuto();
			}
			logger.info("---------------------------INIT META REQUEST JOB HAS BEEN TRIGGERED---------------------------");
			sender.getEventList(DateTime.now().minus(Duration.standardHours(12).getMillis()), DateTime.now().plus(Duration.standardHours(12).getMillis()), true);
		}).start();

		//Send a request once an hour to get matches for the next 24 hours;
		onceHourEventRequesterTimer.cancel();
		onceHourEventRequesterTimer = new Timer();
		onceHourEventRequesterTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				logger.info("---------------------------ONCE PER HOUR META REQUEST JOB HAS BEEN TRIGGERED---------------------------");
				sender.getEventList(DateTime.now(), DateTime.now().plus(Duration.standardHours(24).getMillis()), true);
			}
		}, 1000 * 60 * 60, 1000 * 60 * 60);

		//Send a request once per day to get matches for the next week;
		oncePerDayEventRequesterTimer.cancel();
		oncePerDayEventRequesterTimer = new Timer();
		oncePerDayEventRequesterTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				logger.info("---------------------------ONCE PER DAY META REQUEST JOB HAS BEEN TRIGGERED---------------------------");
				sender.getEventList(DateTime.now(), DateTime.now().plus(Duration.standardHours(24 * 7).getMillis()), true);
			}
		}, 1000 * 60 * 60 * 24, 1000 * 60 * 60 * 24);
		BetRadarState.INSTANCE.triggerOnInitFlag.set(true);
	}

	@Override
	public void onBetCancel(T sender, BetCancelEntity entity) {
		if(!BetRadarState.INSTANCE.feedOpened.get()){
			return;
		}
		Optional<MatchInfoEntity> existingMatchInfoEntity = DataContainer.INSTANCE.getIcelandMetaInfoByEventId(entity.getEventHeader().getEventId());
		if(existingMatchInfoEntity.isPresent()){
			Map<String, Object> betCancelMessage = new HashMap<>();
			betCancelMessage.put("betCancelEntity", entity);
			betCancelMessage.put("homeTeam", existingMatchInfoEntity.get().getHomeTeam());
			betCancelMessage.put("awayTeam", existingMatchInfoEntity.get().getAwayTeam());
			IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ON_BET_CANCEL, JSON.INSTANCE.gson.toJson(betCancelMessage));
		}
	}

	@Override
	public void onBetCancelUndone(T sender, BetCancelUndoEntity entity) {
		if(!BetRadarState.INSTANCE.feedOpened.get()){
			return;
		}
		IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ON_BET_CANCEL_UNDONE, JSON.INSTANCE.gson.toJson(entity));
	}

	@Override
	public void onBetClear(T sender, BetClearEntity entity) {
		if(!BetRadarState.INSTANCE.feedOpened.get()){
			return;
		}
		IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ON_BET_CLEAR, JSON.INSTANCE.gson.toJson(entity));
		LiveBetResultsNormalPersist.INSTANCE.persistBetClearResultsIntoDb(entity);
	}

	@Override
	public void onBetClearRollback(T sender, BetClearRollbackEntity entity) {
		if(!BetRadarState.INSTANCE.feedOpened.get()){
			return;
		}
		IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ON_BET_CLEAR_ROLL_BACK, JSON.INSTANCE.gson.toJson(entity));
		LiveBetResultsRollBackPersist.INSTANCE.persistBetClearResultsIntoDb(entity);
	}

	@Override
	public void onBetStart(T sender, BetStartEntity entity) {
		if(!BetRadarState.INSTANCE.feedOpened.get()){
			return;
		}
		IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ON_BET_START, JSON.INSTANCE.gson.toJson(entity));
	}

	@Override
	public void onBetStop(T sender, BetStopEntity entity) {
		if(!BetRadarState.INSTANCE.feedOpened.get()){
			return;
		}
		IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ON_BET_STOP, JSON.INSTANCE.gson.toJson(entity));
	}

	@Override
	public void onEventMessagesReceived(T sender, EventDataPackage entity) {
		if(!BetRadarState.INSTANCE.feedOpened.get()){
			return;
		}
		IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ON_EVENT_MESSAGE_RECEIVED, JSON.INSTANCE.gson.toJson(entity));
	}

	@Override
	public void onEventStatusReceived(T sender, EventDataPackage entity) {
		if(!BetRadarState.INSTANCE.feedOpened.get()){
			return;
		}
		IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ON_EVENT_STATUS_RECEIVED, JSON.INSTANCE.gson.toJson(entity));
	}

	@Override
	public void onIrrelevantOddsChange(T sender, IrrelevantOddsChangeEntity entity) {
		if(!BetRadarState.INSTANCE.feedOpened.get()){
			return;
		}
		IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ON_IRRELEVANT_ODDS_CHANGE, JSON.INSTANCE.gson.toJson(entity));
	}

	@Override
	public void onMetaInfoReceived(T sender, MetaInfoEntity entity) {
		if(!BetRadarState.INSTANCE.feedOpened.get()){
			return;
		}
		List<MatchHeaderInfoEntity> matches = ((LiveOddsMetaData) entity.getMetaInfoDataContainer()).getMatchHeaderInfos();
		logger.info("On meta info with {} matches", matches.size());
		DataContainer.INSTANCE.setIcelandMetaCache(matches);
	}

	@Override
	public void onOddsChange(T sender, OddsChangeEntity entity) {
		if(!BetRadarState.INSTANCE.feedOpened.get()){
			return;
		}
		DataContainer.INSTANCE.setIcelandTemplateOdds(entity);
		Optional<MatchInfoEntity> existingMatchInfoEntity = DataContainer.INSTANCE.getIcelandMetaInfoByEventId(entity.getEventHeader().getEventId());
		if(existingMatchInfoEntity.isPresent()){
			Map<String, Object> oddsChangeMessage = new HashMap<>();
			oddsChangeMessage.put("homeTeam", existingMatchInfoEntity.get().getHomeTeam());
			oddsChangeMessage.put("awayTeam", existingMatchInfoEntity.get().getAwayTeam());
			List<OddsEntity> activeEventOdds = new ArrayList<>(entity.getEventOdds().stream().filter(OddsEntity::isActive).collect(Collectors.toList()));
			Collections.sort(activeEventOdds, (o1, o2) -> calculateOddValue(o2).compareTo(calculateOddValue(o1)));
			List<IcelandOddsEntity> sortedInjectedEventOdds = activeEventOdds.stream().map(oddsEntity -> {
				IcelandOddsEntity icelandOddsEntity = new IcelandOddsEntity();
				try{
					BeanUtils.copyProperties(icelandOddsEntity, oddsEntity);
					GroupNameOrderMapper groupNameOrderMapper = new GroupNameOrderMapper(oddsEntity).invoke();
					icelandOddsEntity.setGroupName(groupNameOrderMapper.getGroupName());

				} catch (IllegalAccessException | InvocationTargetException e){
					e.printStackTrace();
				}
				return icelandOddsEntity;
			}).collect(Collectors.toList());
			oddsChangeMessage.put("eventOdds",sortedInjectedEventOdds);
			oddsChangeMessage.put("eventHeader",entity.getEventHeader());
			oddsChangeMessage.put("messages",entity.getMessages());
			IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ON_ODDS_CHANGE, JSON.INSTANCE.gson.toJson(oddsChangeMessage));
		}
	}


}
