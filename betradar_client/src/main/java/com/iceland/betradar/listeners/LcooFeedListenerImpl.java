package com.iceland.betradar.listeners;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.DbUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iceland.betradar.dao.IcelandDataSource;
import com.iceland.betradar.service.persist.lcoo.LcooMatchResultsPersist;
import com.iceland.betradar.service.persist.lcoo.LcooOutRigthsResultPersist;
import com.iceland.betradar.service.persist.lcoo.LcooPersist;
import com.iceland.betradar.service.Util;
import com.sportradar.sdk.common.enums.FeedEventType;
import com.sportradar.sdk.feed.lcoo.entities.BatchCompleted;
import com.sportradar.sdk.feed.lcoo.entities.MatchEntity;
import com.sportradar.sdk.feed.lcoo.entities.OutrightEntity;
import com.sportradar.sdk.feed.lcoo.entities.OutrightResultEntity;
import com.sportradar.sdk.feed.lcoo.entities.OutrightsEntity;
import com.sportradar.sdk.feed.lcoo.entities.TextEntity;
import com.sportradar.sdk.feed.lcoo.entities.TextsEntity;
import com.sportradar.sdk.feed.lcoo.entities.ThreeBallEntity;
import com.sportradar.sdk.feed.lcoo.interfaces.LcooFeed;
import com.sportradar.sdk.feed.lcoo.interfaces.LcooFeedListener;

public class LcooFeedListenerImpl implements LcooFeedListener {

	private final static Logger logger = LoggerFactory.getLogger(example.listeners.LcooFeedListenerImpl.class);

	@Override
	public void onBatchCompleted(LcooFeed lcooFeed, BatchCompleted batchCompleted) {
		//Only used for two phase commit.
	}

	@Override
	public void onMatchReceived(LcooFeed sender, final MatchEntity match) {
		if(!CollectionUtils.isEmpty(match.getBetResults())){
			LcooMatchResultsPersist.INSTANCE.persistMatchResults(match);
			return;
		}
		Connection connection = null;
		try{
			DateTime now = DateTime.now();
			connection = IcelandDataSource.INSTANCE.getDs().getConnection();

			//match.fixture -> xml_team
			List<TextsEntity> texts = match.getFixture().getCompetitors().getTexts();
			Integer homeTeamId = 0;
			Integer awayTeamId = 0;
			for(TextsEntity textsEntity : texts){
				for(TextEntity text : textsEntity.getTexts()){
					if(text.getType().equals("1")){
						homeTeamId = text.getId();
						LcooPersist.INSTANCE.mapSportInfoToXmlTeam(match.getSport(), connection, now, homeTeamId, text);
					}
					if(text.getType().equals("2")){
						awayTeamId = text.getId();
						LcooPersist.INSTANCE.mapSportInfoToXmlTeam(match.getSport(), connection, now, awayTeamId, text);
					}
				}
			}

			//match.category -> XmlCountry
			LcooPersist.INSTANCE.mapCategoryToXmlCountry(match.getCategory(), match.getSport(), connection, now);

			//match.fixture -> xml_event
			LcooPersist.INSTANCE.mapMatchInfoToXmlEvent(match, connection, now, homeTeamId, awayTeamId);

			//match.tournament -> xml_league
			
			LcooPersist.INSTANCE.mapMatchTournamentToXmlLeague(match, connection, now);

			//match.sportId , match.odds.odds.type -> xml_bet_type
			LcooPersist.INSTANCE.mapMatchSportAndOddToXmlBetType(match, connection, now);

			//match.odds -> xml_out_come
			LcooPersist.INSTANCE.mapMatchOddsToXmlOutCome(match, connection, now);



		} catch (SQLException e){
			logger.error("DB UPDATE FAILED SQL EXCEPTION", e);
			DbUtils.rollbackAndCloseQuietly(connection);
		} catch (Exception e){
			logger.error("DB UPDATE FAILED", e);
			DbUtils.rollbackAndCloseQuietly(connection);
		} finally{
			DbUtils.commitAndCloseQuietly(connection);
		}

	}

	@Override
	public void onOutrightsReceived(LcooFeed sender, OutrightsEntity outrights) {
		Connection connection = null;
		List<OutrightResultEntity> firstResult = outrights.getOutrightEntities().get(0).getResult();
		if(!CollectionUtils.isEmpty(firstResult)){
			LcooOutRigthsResultPersist.INSTANCE.persistOutRightsResults(outrights);
			return;
		}

		try{
			DateTime now = DateTime.now();
			connection = IcelandDataSource.INSTANCE.getDs().getConnection();
			for(OutrightEntity outrightEntity : outrights.getOutrightEntities()){
				long eventId = (long) outrightEntity.getId();
				Long id = Long.valueOf("999" + Util.randomGen(10000,99999).toString());
				String eventName = outrightEntity.getFixture().getEventInfo().getEventName().get(0).getTexts().get(1).getValue();

				//outrightEntity.fixture -> xml_team
				LcooPersist.INSTANCE.mapOutRightsToXmlTeam(connection, now, outrightEntity);

				//outrightEntity -> xml_league
				LcooPersist.INSTANCE.mapOutRightsToXmlLeague(connection, now, outrightEntity, id, eventName);

				//outrightEntity -> xml_event
				LcooPersist.INSTANCE.mapOutRightsToXmlEvent(connection, now, outrightEntity, id, eventName);

				//outrightEntity -> xml_bet_type
				LcooPersist.INSTANCE.mapOutRightsToXmlBetType(connection,outrightEntity,now);

				//outrightEntity -> xml_outcome
				LcooPersist.INSTANCE.mapOutRightsToXmlOutCome(connection, now, outrightEntity, id, eventId);

				//outrightEntity -> xml_country
				LcooPersist.INSTANCE.mapCategoryToXmlCountry(outrightEntity.getCategory(), outrightEntity.getSport(), connection, now);

			}
		} catch (SQLException e){
			logger.error("DB UPDATE FAILED OUT RIGHTS SQL EXCEPTION", e);
			DbUtils.rollbackAndCloseQuietly(connection);
		} catch (Exception e){
			logger.error("DB UPDATE FAILED OUT RIGHTS", e);
			DbUtils.rollbackAndCloseQuietly(connection);
		} finally{
			DbUtils.commitAndCloseQuietly(connection);
		}
	}

	@Override
	public void onThreeBallReceived(LcooFeed sender, ThreeBallEntity threeBall) {
		logger.info("On three ball with three ball id: {}", threeBall.getId());
	}

	@Override
	public void onClosed(LcooFeed sender) {
		logger.info("On closed");
	}

	@Override
	public void onFeedEvent(LcooFeed sender, FeedEventType eventType) {
		logger.info("Lcoo feed event occurred. Event: {}", eventType);
	}

	@Override
	public void onOpened(LcooFeed sender) {
		logger.info("On opened");
	}

}
