package com.iceland.betradar.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iceland.betradar.model.ProtocolConstants;
import com.iceland.betradar.model.iceland.IcelandCategory;
import com.iceland.betradar.model.iceland.IcelandEvent;
import com.iceland.betradar.model.iceland.IcelandSport;
import com.iceland.betradar.model.iceland.IcelandTemplateData;
import com.iceland.betradar.model.iceland.IcelandTournament;
import com.iceland.betradar.model.iceland.metacontainer.IcelandMatchInfo;
import com.iceland.betradar.model.iceland.oddsentity.IcelandGroupNameOrder;
import com.iceland.betradar.service.persist.lcoo.LcooPersist;
import com.iceland.betradar.service.persist.live.LiveMatchInfoPersist;
import com.sportradar.sdk.feed.common.entities.LocalizedString;
import com.sportradar.sdk.feed.liveodds.entities.common.AliveEntity;
import com.sportradar.sdk.feed.liveodds.entities.common.EventHeaderEntity;
import com.sportradar.sdk.feed.liveodds.entities.common.OddsChangeEntity;
import com.sportradar.sdk.feed.liveodds.entities.common.OddsEntity;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.MatchHeaderEntity;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.MatchHeaderInfoEntity;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.MatchInfoEntity;
import com.sportradar.sdk.feed.liveodds.enums.OddsType;

/**
 * A utility singleton class for storing and emitting iceland specific data.
 */
public enum DataContainer {

	INSTANCE;

	private final Logger logger = LoggerFactory.getLogger(DataContainer.class);
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	/**
	 * A template match info data which is grouped by sports, category, tournament.
	 */
	private IcelandTemplateData icelandTemplateData = new IcelandTemplateData();

	/**
	 * For storing last alive entity.
	 */
	private AliveEntity lastReceivedAliveEntity = null;

	/**
	 * A cache for storing mainly match information and restore it back using event id.
	 */
	private Map<Long, MatchHeaderInfoEntity> icelandMetaCache = new ConcurrentHashMap<>();

	/**
	 * A private method for refreshing template data on socket server.
	 * This time it is guaranteed that iceland template data on socket server should not include any empty nodes.
	 */
	private void refreshIcelandTemplateData() {
		icelandTemplateData.removeEmptyNodes();
		String jsonStr = JSON.INSTANCE.gson.toJson(icelandTemplateData);
		IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.REFRESH_TEMPLATE_DATA_ON_SOCKET_SERVER, jsonStr);
	}

	/**
	 * A private method for adding an odd to iceland odds cache.
	 * @param entity Odds change entitys
	 * @param existingIcelandEvent Existing event
	 */
	private void setIcelandOddsCacheData(OddsChangeEntity entity, IcelandEvent existingIcelandEvent) {
		Long eventId = entity.getEventHeader().getEventId();
		MatchHeaderInfoEntity matchHeaderInfoEntity = icelandMetaCache.get(eventId);
		List<OddsEntity> oddsEntities = entity.getEventOdds();
		List<OddsEntity> activeOddsEntities = oddsEntities.stream().filter(OddsEntity::isActive).collect(Collectors.toList());
		Map<IcelandGroupNameOrder, List<OddsEntity>> groupedActiveOddsEntities = new TreeMap<>((o1, o2) -> o1.getGroupOrder().compareTo(o2.getGroupOrder()));
		for(OddsEntity activeOddsEntity : activeOddsEntities){
			GroupNameOrderMapper groupNameOrderMapper = new GroupNameOrderMapper(activeOddsEntity).invoke();
			IcelandGroupNameOrder icelandGroupNameOrder = new IcelandGroupNameOrder();
			icelandGroupNameOrder.setGroupOrder(groupNameOrderMapper.getGroupOrder());
			icelandGroupNameOrder.setGroupName(groupNameOrderMapper.getGroupName());
			if(groupedActiveOddsEntities.containsKey(icelandGroupNameOrder)){
				groupedActiveOddsEntities.get(icelandGroupNameOrder).add(activeOddsEntity);
			} else {
				List<OddsEntity> oneOddsEntityArray = new ArrayList<>();
				oneOddsEntityArray.add(activeOddsEntity);
				groupedActiveOddsEntities.put(icelandGroupNameOrder, oneOddsEntityArray);
			}
		}
		Map<String, List<OddsEntity>> orderedActiveOddsEntities = new LinkedHashMap<>();
		for(IcelandGroupNameOrder icelandGroupNameOrder : groupedActiveOddsEntities.keySet()){
			orderedActiveOddsEntities.put(icelandGroupNameOrder.getGroupName(), groupedActiveOddsEntities.get(icelandGroupNameOrder));
		}

		Map<String, Object> oddsCacheEntity = new HashMap<>();
		oddsCacheEntity.put("eventId", eventId);
		oddsCacheEntity.put("eventHeader",entity.getEventHeader());
		oddsCacheEntity.put("activeOddsEntities", orderedActiveOddsEntities);
		oddsCacheEntity.put("homeTeam", matchHeaderInfoEntity.getMatchInfo().getHomeTeam().getName());
		oddsCacheEntity.put("awayTeam", matchHeaderInfoEntity.getMatchInfo().getAwayTeam().getName());
		oddsCacheEntity.put("betStatus", existingIcelandEvent.getEventHeader().getBetStatus());
		IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.REFRESH_ODDS_CACHE_ON_SOCKET_SERVER, JSON.INSTANCE.gson.toJson(oddsCacheEntity));
	}

	/**
	 * A private method for removing an odd from odds cache.
	 * @param eventId Event to be removed.
	 */
	private void removeIcelandOddsCacheData(Long eventId) {
		IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.REMOVE_ICELAND_ODDS_CACHE_DATA, eventId.toString());
	}

	/**
	 * A private method for sending the difference data on alive events.
	 * @param removedAliveEvents Alive events marked as removed.
	 * @param changedAliveEvents Alive events marked as changed. This events exists previously on template data.
	 * @param newAliveEventsWithExistingTournament Alive events that comes to an existing tournament.
	 * @param newAliveEventsWithExistingCategory Alive events that comes to an existing category.
	 * @param newAliveEventsWithExistingSport Alive events that comes to an existing sport.
	 * @param newAliveEventsWithNewSport Alive events that comes a new sport.
	 */
	private void emitAliveChanges(List<Object> removedAliveEvents, List<Object> changedAliveEvents, List<Object> newAliveEventsWithExistingTournament, List<Object> newAliveEventsWithExistingCategory,
			List<Object> newAliveEventsWithExistingSport, List<Object> newAliveEventsWithNewSport) {
		if(removedAliveEvents.size() > 0){
			String jsonStr = JSON.INSTANCE.gson.toJson(removedAliveEvents);
			IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ALIVE_REMOVED, jsonStr);
		}
		if(changedAliveEvents.size() > 0){
			String jsonStr = JSON.INSTANCE.gson.toJson(changedAliveEvents);
			IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ALIVE_CHANGED, jsonStr);
		}
		if(newAliveEventsWithExistingTournament.size() > 0){
			String jsonStr = JSON.INSTANCE.gson.toJson(newAliveEventsWithExistingTournament);
			IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ALIVE_NEW_ALIVE_EXISTING_TOURNAMENT_RECEIVED, jsonStr);
		}
		if(newAliveEventsWithExistingCategory.size() > 0){
			String jsonStr = JSON.INSTANCE.gson.toJson(newAliveEventsWithExistingCategory);
			IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ALIVE_NEW_ALIVE_EXISTING_CATEGORY, jsonStr);
		}
		if(newAliveEventsWithExistingSport.size() > 0){
			String jsonStr = JSON.INSTANCE.gson.toJson(newAliveEventsWithExistingSport);
			IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ALIVE_NEW_ALIVE_EXISTING_SPORT, jsonStr);
		}
		if(newAliveEventsWithNewSport.size() > 0){
			String jsonStr = JSON.INSTANCE.gson.toJson(newAliveEventsWithNewSport);
			IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ALIVE_NEW_SPORT, jsonStr);
		}
	}

	/**
	 * A method for setting iceland template data.
	 * If an alive event that is not in last alive events, then it should be removed from the list and all the odds of it should also be deleted.
	 * After removing such data then newly arrived data is grouped and positioned into the existing template data.
	 * When positioning difference data is recorded and sent to the clients via socket server.
	 * @param incomingEntity Alive incomingEntity
	 */
	public void setIcelandTemplateData(AliveEntity incomingEntity) {
		List<Object> removedAliveEvents = new ArrayList<>();
		List<Object> changedAliveEvents = new ArrayList<>();
		List<Object> newAliveEventsWithExistingTournament = new ArrayList<>();
		List<Object> newAliveEventsWithExistingCategory = new ArrayList<>();
		List<Object> newAliveEventsWithExistingSport = new ArrayList<>();
		List<Object> newAliveEventsWithNewSport = new ArrayList<>();
		if(lastReceivedAliveEntity != null){
			logger.info("last received alive count : {}  new alive count : {} ", lastReceivedAliveEntity.getEventHeaders().size(), incomingEntity.getEventHeaders().size());
			for(EventHeaderEntity lastReceivedEventHeader : lastReceivedAliveEntity.getEventHeaders()){
				boolean lastAliveIdExistsInIncoming = incomingEntity.getEventHeaders()
						.stream()
						.anyMatch(incomingEventHeader -> incomingEventHeader.getEventId().equals(lastReceivedEventHeader.getEventId()));
				if(!lastAliveIdExistsInIncoming){
					Long lastReceivedEventId = lastReceivedEventHeader.getEventId();
					if(icelandMetaCache.containsKey(lastReceivedEventId)){
						MatchInfoEntity existingMatchInfoEntity = icelandMetaCache.get(lastReceivedEventId).getMatchInfo();
						Map<String, Object> removedAliveEvent = new HashMap<>();
						removedAliveEvent.put("eventId", lastReceivedEventId);
						removedAliveEvent.put("homeTeam", existingMatchInfoEntity.getHomeTeam().getName());
						removedAliveEvent.put("awayTeam", existingMatchInfoEntity.getAwayTeam().getName());
						removedAliveEvents.add(removedAliveEvent);
					}
					Optional<IcelandTournament> targetIcelandTournament = icelandTemplateData.getIcelandTournamentEventById(lastReceivedEventHeader.getEventId());
					removeIcelandOddsCacheData(lastReceivedEventHeader.getEventId());
					if(targetIcelandTournament.isPresent()){
						targetIcelandTournament.get().events.remove(lastReceivedEventHeader.getEventId());
					}
				}
			}
		}

		for(EventHeaderEntity eventHeaderEntity : incomingEntity.getEventHeaders()){
			Long eventId = eventHeaderEntity.getEventId();
			if(!icelandMetaCache.containsKey(eventId)){
				logger.warn("A NON EXISTING ALIVE ENTITY HAS ARRIVED: " + eventId);
				continue;
			}
			MatchInfoEntity existingMatchInfoEntity = icelandMetaCache.get(eventId).getMatchInfo();
			MatchHeaderEntity incomingMatchHeaderEntity = (MatchHeaderEntity) eventHeaderEntity;

			IcelandEvent incomingIcelandEvent = new IcelandEvent();
			incomingIcelandEvent.setEventId(eventId);
			incomingIcelandEvent.setEventHeader(incomingMatchHeaderEntity);
			IcelandMatchInfo icelandMatchInfo = new IcelandMatchInfo();
			icelandMatchInfo.setHomeTeam(existingMatchInfoEntity.getHomeTeam());
			icelandMatchInfo.setDateOfMatch(existingMatchInfoEntity.getDateOfMatch());
			icelandMatchInfo.setAwayTeam(existingMatchInfoEntity.getAwayTeam());
			incomingIcelandEvent.setMatchInfo(icelandMatchInfo);

			Long tournamentId = existingMatchInfoEntity.getTournament().getId();
			LocalizedString tournamentName = existingMatchInfoEntity.getTournament().getName();
			IcelandTournament incomingIcelandTournament = new IcelandTournament(tournamentId, tournamentName);

			Long categoryId = existingMatchInfoEntity.getCategory().getId();
			LocalizedString categoryName = existingMatchInfoEntity.getCategory().getName();
			IcelandCategory incomingIcelandCategory = new IcelandCategory(categoryId, categoryName);

			Long sportId = existingMatchInfoEntity.getSport().getId();
			LocalizedString sportName = existingMatchInfoEntity.getSport().getName();
			IcelandSport incomingIcelandSport = new IcelandSport(sportId, sportName);

			if(icelandTemplateData.sports.containsKey(sportId)){
				IcelandSport existingSport = icelandTemplateData.sports.get(sportId);
				if(existingSport.icelandCategories.containsKey(categoryId)){
					IcelandCategory existingCategory = existingSport.icelandCategories.get(categoryId);
					if(existingCategory.icelandTournaments.containsKey(tournamentId)){
						IcelandTournament existingTournament = existingCategory.icelandTournaments.get(tournamentId);
						if(existingTournament.events.containsKey(eventId)){
							//At this point en existing alive event has arrived.
							IcelandEvent existingIcelandEvent = existingTournament.events.get(eventId);
							String existingIcelandEventHeaderJson = JSON.INSTANCE.gson.toJson(existingIcelandEvent.getEventHeader());
							String incomingIcelandEventHeaderJson = JSON.INSTANCE.gson.toJson(incomingIcelandEvent.getEventHeader());
							if(!existingIcelandEventHeaderJson.equals(incomingIcelandEventHeaderJson)){
								//At this point an existing alive entity has changed.
								Map<String, Object> changedAliveEvent = new HashMap<>();
								changedAliveEvent.put("existingEventId", eventId);
								changedAliveEvent.put("changedEventHeader", incomingIcelandEvent.getEventHeader());
								changedAliveEvents.add(changedAliveEvent);
							}
							existingIcelandEvent.setEventHeader(incomingIcelandEvent.getEventHeader());
						} else {
							//At this point a new alive event with an existing tournament has arrived.
							Map<String, Object> newAliveEvent = new HashMap<>();
							newAliveEvent.put("newEventId", eventId);
							newAliveEvent.put("newEventHeader", incomingIcelandEvent.getEventHeader());
							newAliveEvent.put("matchInfo", incomingIcelandEvent.getMatchInfo());
							newAliveEvent.put("existingTournamentId", existingTournament.getId());
							newAliveEvent.put("existingCategoryId", existingCategory.getId());
							newAliveEvent.put("existingSportId", existingSport.getId());
							newAliveEventsWithExistingTournament.add(newAliveEvent);

							existingTournament.events.put(eventId, incomingIcelandEvent);
						}
					} else {
						//At this point a new alive event with existing category has arrived.
						Map<String, Object> newAliveEvent = new HashMap<>();
						newAliveEvent.put("newEventId", eventId);
						newAliveEvent.put("newEventHeader", incomingIcelandEvent.getEventHeader());
						newAliveEvent.put("matchInfo", incomingIcelandEvent.getMatchInfo());
						newAliveEvent.put("newTournamentId", incomingIcelandTournament.getId());
						newAliveEvent.put("newTournamentName", incomingIcelandTournament.getName());
						newAliveEvent.put("existingCategoryId", existingCategory.getId());
						newAliveEvent.put("existingCategoryName", existingCategory.getName());
						newAliveEvent.put("existingSportId", existingSport.getId());
						newAliveEventsWithExistingCategory.add(newAliveEvent);

						incomingIcelandTournament.events.put(eventId, incomingIcelandEvent);
						existingCategory.icelandTournaments.put(tournamentId, incomingIcelandTournament);
					}
				} else {
					//At this point a new alive event with existing sport has arrived.
					Map<String, Object> newAliveEvent = new HashMap<>();
					newAliveEvent.put("newEventId", eventId);
					newAliveEvent.put("newEventHeader", incomingIcelandEvent.getEventHeader());
					newAliveEvent.put("matchInfo", incomingIcelandEvent.getMatchInfo());
					newAliveEvent.put("newTournamentId", incomingIcelandTournament.getId());
					newAliveEvent.put("newTournamentName", incomingIcelandTournament.getName());
					newAliveEvent.put("newCategoryId", incomingIcelandCategory.getId());
					newAliveEvent.put("newCategoryName", incomingIcelandCategory.getName());
					newAliveEvent.put("existingSportId", existingSport.getId());
					newAliveEventsWithExistingSport.add(newAliveEvent);

					incomingIcelandTournament.events.put(eventId, incomingIcelandEvent);
					incomingIcelandCategory.icelandTournaments.put(tournamentId, incomingIcelandTournament);
					existingSport.icelandCategories.put(categoryId, incomingIcelandCategory);
				}
			} else {
				//At this point a new alive event with a new sport has arrived.
				Map<String, Object> newAliveEvent = new HashMap<>();
				newAliveEvent.put("newEventId", eventId);
				newAliveEvent.put("newEventHeader", incomingIcelandEvent.getEventHeader());
				newAliveEvent.put("matchInfo", incomingIcelandEvent.getMatchInfo());
				newAliveEvent.put("newTournamentId", incomingIcelandTournament.getId());
				newAliveEvent.put("newTournamentName", incomingIcelandTournament.getName());
				newAliveEvent.put("newCategoryId", incomingIcelandCategory.getId());
				newAliveEvent.put("newCategoryName", incomingIcelandCategory.getName());
				newAliveEvent.put("newSportId", incomingIcelandSport.getId());
				newAliveEvent.put("newSportName", incomingIcelandSport.getName());
				newAliveEventsWithNewSport.add(newAliveEvent);

				incomingIcelandTournament.events.put(eventId, incomingIcelandEvent);
				incomingIcelandCategory.icelandTournaments.put(tournamentId, incomingIcelandTournament);
				incomingIcelandSport.icelandCategories.put(categoryId, incomingIcelandCategory);
				icelandTemplateData.sports.put(sportId, incomingIcelandSport);
			}
		}

		emitAliveChanges(removedAliveEvents, changedAliveEvents, newAliveEventsWithExistingTournament, newAliveEventsWithExistingCategory, newAliveEventsWithExistingSport, newAliveEventsWithNewSport);

		//Always refresh the template data on any conditions.
		refreshIcelandTemplateData();
		lastReceivedAliveEntity = incomingEntity;
		if(BetRadarState.INSTANCE.triggerOnInitFlag.get()){
			BetRadarState.INSTANCE.triggerOnInitFlag.set(false);
			scheduler.schedule(() -> {
				IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.BET_RADAR_CLIENT_ON_INITIALIZED, "critical");
			}, 10, TimeUnit.SECONDS);
		}
	}

	/**
	 * A method for setting iceland template meta cache.
	 * @param matches Match info data that comes from meta data.
	 */
	public void setIcelandMetaCache(List<MatchHeaderInfoEntity> matches) {
		for(MatchHeaderInfoEntity matchHeaderInfoEntity : matches){
			Long eventId = matchHeaderInfoEntity.getMatchHeader().getEventId();
			icelandMetaCache.put(eventId, matchHeaderInfoEntity);
		}
		LiveMatchInfoPersist.INSTANCE.persistMatchInfo(matches);
	}

	/**
	 * The method first calculate the following entities for a specific event id:
	 *  1. total active incoming odds.
	 *  2. Two way or three way active incoming odds
	 *  3. First active incoming odd which has odds field size less then three.
	 *  4. First most popular incoming odd which has odds field size less then three.
	 * Then it calculate the incoming prime odd candidate with the following algorithm :
	 *  1. If there is no two way or three way odds then assign the prime odd as the first most popular odd
	 *  2. If there is no most popular then assign the prime odd as the first active incoming odd.
	 *  3. Else try to set a three way odd as prime odd. First three way odd is the prime odd.
	 *  4. Else try to set a two way odd as prime odd. First two way odd is the prime odd.
	 *  //10L three way 20L two way
	 * @param entity Incoming odds change entity.
	 */
	public void setIcelandTemplateOdds(OddsChangeEntity entity) {
		Optional<IcelandEvent> icelandEvent = icelandTemplateData.getIcelandEventById(entity.getEventHeader().getEventId());
		if(icelandEvent.isPresent()){
			DataContainer.INSTANCE.setIcelandOddsCacheData(entity, icelandEvent.get());

			IcelandEvent existingIcelandEvent = icelandEvent.get();
			MatchHeaderInfoEntity existingIcelandEventMatchInfo = icelandMetaCache.get(entity.getEventHeader().getEventId());

			//Calculate first active and two way or three way active incoming odds.
			List<OddsEntity> incomingEventOdds = entity.getEventOdds();
			List<OddsEntity> twoWayOrThreeWayActiveIncomingOdds = new ArrayList<>();
			Integer totalActiveCount = 0;
			boolean searchActiveOdd = true;
			OddsEntity firstActiveIncomingOdd = null;
			boolean searchMostPopularOdd = true;
			OddsEntity firstMostPopularOdd = null;
			boolean isIncomingOddsHasTargetOdds = false;

			for(OddsEntity incomingEventOdd : incomingEventOdds){
				if(incomingEventOdd.isActive()){
					totalActiveCount += 1;
					if(incomingEventOdd.getOddFields().size() <= 3){
						if(searchMostPopularOdd && incomingEventOdd.getSubType() != null && LcooPersist.INSTANCE.getGroupNameByOddsType(incomingEventOdd.getSubType().intValue())
								.equals("popular_market")){
							searchMostPopularOdd = false;
							firstMostPopularOdd = incomingEventOdd;
						}
						if(searchActiveOdd){
							searchActiveOdd = false;
							firstActiveIncomingOdd = incomingEventOdd;
						}
					}

					if(incomingEventOdd.getType() == OddsType.ITEM3W || incomingEventOdd.getType() == OddsType.FT2W || incomingEventOdd.getType() == OddsType.FT3W){
						twoWayOrThreeWayActiveIncomingOdds.add(incomingEventOdd);
						isIncomingOddsHasTargetOdds = true;
					}
				}
			}

			//Try to fetch the existing prime odd from the newly arrived data and if it is changed emit the relevant data and exit.
			if(!isIncomingOddsHasTargetOdds && existingIcelandEvent.getPrimeOdd() != null){
				Long existingPrimeOddId = existingIcelandEvent.getPrimeOdd().getId();
				Optional<OddsEntity> incomingPrimeOddsEntity = entity.getEventOdds().stream().filter(oddsEntity -> oddsEntity.getId() == existingPrimeOddId).findFirst();
				if(incomingPrimeOddsEntity.isPresent() && incomingPrimeOddsEntity.get().isActive()){
					Map<String, Object> emitMessage = new HashMap<>();
					emitMessage.put("eventId", entity.getEventHeader().getEventId());
					emitMessage.put("changedOdd", incomingPrimeOddsEntity);
					emitMessage.put("existingPrimeOddHasChanged", true);
					emitMessage.put("totalActiveOdds", totalActiveCount);
					emitMessage.put("homeTeam", existingIcelandEventMatchInfo.getMatchInfo().getHomeTeam().getName());
					emitMessage.put("awayTeam", existingIcelandEventMatchInfo.getMatchInfo().getAwayTeam().getName());
					emitMessage.put("eventHeader", entity.getEventHeader());
					IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.PRIME_ODDS_CHANGE, JSON.INSTANCE.gson.toJson(emitMessage));
					existingIcelandEvent.setPrimeOdd(incomingPrimeOddsEntity.get());
					existingIcelandEvent.setActiveOddsCount(totalActiveCount);
					refreshIcelandTemplateData();
					return;
				}
			}

			//Calculate the prime odd candidate.
			Optional<OddsEntity> incomingPrimeOddCandidate = Optional.empty();

			//If there is no (two way) or (three way) arriving odds, then set the first most popular active odd
			//If there is no popular then set the first active odd.
			if(twoWayOrThreeWayActiveIncomingOdds.size() == 0){
				if(!searchMostPopularOdd){
					incomingPrimeOddCandidate = Optional.of(firstMostPopularOdd);
				} else if(!searchActiveOdd){
					incomingPrimeOddCandidate = Optional.of(firstActiveIncomingOdd);
				}
			} else if(twoWayOrThreeWayActiveIncomingOdds.size() == 1){
				incomingPrimeOddCandidate = Optional.of(twoWayOrThreeWayActiveIncomingOdds.get(0));
			} else {
				//First try to set a item three way odd
				for(OddsEntity twoWayOrThreeWayEventOdd : twoWayOrThreeWayActiveIncomingOdds){
					if(twoWayOrThreeWayEventOdd.getType() == OddsType.ITEM3W){
						incomingPrimeOddCandidate = Optional.of(twoWayOrThreeWayEventOdd);
						break;
					}
				}
				//Second try to set a two way odd
				if(!incomingPrimeOddCandidate.isPresent()){
					for(OddsEntity twoWayOrThreeWayEventOdd : twoWayOrThreeWayActiveIncomingOdds){
						if(twoWayOrThreeWayEventOdd.getType() == OddsType.FT2W){
							incomingPrimeOddCandidate = Optional.of(twoWayOrThreeWayEventOdd);
							break;
						}
					}
				}
				//Third try to set a three way odd
				if(!incomingPrimeOddCandidate.isPresent()){
					for(OddsEntity twoWayOrThreeWayEventOdd : twoWayOrThreeWayActiveIncomingOdds){
						if(twoWayOrThreeWayEventOdd.getType() == OddsType.FT3W){
							incomingPrimeOddCandidate = Optional.of(twoWayOrThreeWayEventOdd);
							break;
						}
					}
				}
			}

			//Determine if it is changed and emit the events.
			if(incomingPrimeOddCandidate.isPresent()){
				String existingOddsJson = JSON.INSTANCE.gson.toJson(existingIcelandEvent.getPrimeOdd());
				String arrivingOddsJson = JSON.INSTANCE.gson.toJson(incomingPrimeOddCandidate.get());
				if(!existingOddsJson.equals(arrivingOddsJson) || !existingIcelandEvent.getActiveOddsCount().equals(totalActiveCount)){
					Map<String, Object> emitMessage = new HashMap<>();
					emitMessage.put("eventId", entity.getEventHeader().getEventId());
					emitMessage.put("changedOdd", incomingPrimeOddCandidate.get());
					if(existingIcelandEvent.getPrimeOdd() == null){
						emitMessage.put("newPrimeOddHasArrived", true);
					} else {
						emitMessage.put("newPrimeOddHasArrived", false);
					}
					emitMessage.put("existingPrimeOddHasChanged", false);
					emitMessage.put("totalActiveOdds", totalActiveCount);
					emitMessage.put("homeTeam", existingIcelandEventMatchInfo.getMatchInfo().getHomeTeam().getName());
					emitMessage.put("awayTeam", existingIcelandEventMatchInfo.getMatchInfo().getAwayTeam().getName());
					emitMessage.put("eventHeader", entity.getEventHeader());
					IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.PRIME_ODDS_CHANGE, JSON.INSTANCE.gson.toJson(emitMessage));
				}
				existingIcelandEvent.setPrimeOdd(incomingPrimeOddCandidate.get());
				existingIcelandEvent.setActiveOddsCount(totalActiveCount);
			} else {
				logger.warn("PRIME ODD CANDIDATE CAN NOT BE CALCULATED : " + entity.getEventHeader().getEventId());
				existingIcelandEvent.setPrimeOdd(null);
				existingIcelandEvent.setActiveOddsCount(0);
				Map<String, Object> emitMessage = new HashMap<>();
				emitMessage.put("eventId", entity.getEventHeader().getEventId());
				emitMessage.put("totalActiveOdds", totalActiveCount);
				emitMessage.put("homeTeam", existingIcelandEventMatchInfo.getMatchInfo().getHomeTeam().getName());
				emitMessage.put("awayTeam", existingIcelandEventMatchInfo.getMatchInfo().getAwayTeam().getName());
				emitMessage.put("eventHeader", entity.getEventHeader());
				IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.PRIME_ODDS_DELETED, JSON.INSTANCE.gson.toJson(emitMessage));
			}
			refreshIcelandTemplateData();
		} else {
			logger.warn("A NON EXISTING ODDS FIELD HAS ARRIVED");
		}
	}

	public Optional<MatchInfoEntity> getIcelandMetaInfoByEventId(Long eventId) {
		if(icelandMetaCache.get(eventId) == null){
			return Optional.empty();
		}
		return Optional.of(icelandMetaCache.get(eventId).getMatchInfo());
	}

	public void clearDataContainer() {
		icelandTemplateData = new IcelandTemplateData();
		lastReceivedAliveEntity = null;
		icelandMetaCache = new ConcurrentHashMap<>();
	}

}
