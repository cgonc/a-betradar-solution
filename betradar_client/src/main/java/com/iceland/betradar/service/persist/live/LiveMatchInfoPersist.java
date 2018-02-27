package com.iceland.betradar.service.persist.live;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iceland.betradar.dao.IcelandDataSource;
import com.iceland.betradar.dao.impl.XmlCountry;
import com.iceland.betradar.dao.impl.XmlCountryDao;
import com.iceland.betradar.dao.impl.XmlEvent;
import com.iceland.betradar.dao.impl.XmlEventDao;
import com.iceland.betradar.dao.impl.XmlLeague;
import com.iceland.betradar.dao.impl.XmlLeagueDao;
import com.iceland.betradar.dao.impl.XmlTeam;
import com.iceland.betradar.dao.impl.XmlTeamDao;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.MatchHeaderInfoEntity;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.MatchInfoEntity;

/**
 * Some times there can be situations where alive entities contains more event data then pre live data.
 * This service is for this kind of things. When it finds more data it persists this data for pre live events.
 */
public enum LiveMatchInfoPersist {
	INSTANCE;

	private final static Logger logger = LoggerFactory.getLogger(LiveMatchInfoPersist.class);

	public void persistMatchInfo(final List<MatchHeaderInfoEntity> matches) {
		new Thread(() -> {
			DateTime now = DateTime.now();
			for(MatchHeaderInfoEntity matchHeaderInfoEntity : matches){
				Connection connection = null;
				try{
					connection = IcelandDataSource.INSTANCE.getDs().getConnection();
					MatchInfoEntity matchInfo = matchHeaderInfoEntity.getMatchInfo();

					Long eventId = matchHeaderInfoEntity.getMatchHeader().getEventId();
					XmlEvent xmlEvent = XmlEventDao.INSTANCE.findById(connection, eventId);
					if(xmlEvent != null){
						continue;
					}

					//MatchHeaderEntity to xml_team
					Long homeTeamId = matchInfo.getHomeTeam().getId();
					XmlTeam xmlTeamHome = XmlTeamDao.INSTANCE.findById(connection, homeTeamId);
					if(xmlTeamHome == null){
						xmlTeamHome = new XmlTeam();
						xmlTeamHome.setId(homeTeamId);
						xmlTeamHome.setName(matchInfo.getHomeTeam().getName().getTranslation("en"));
						xmlTeamHome.setSportId(matchInfo.getSport().getId());
						xmlTeamHome.setCreatedAt(now.toDate());
						xmlTeamHome.setUpdatedAt(now.toDate());
						XmlTeamDao.INSTANCE.insert(connection, xmlTeamHome);
					}

					Long awayTeamId = matchInfo.getAwayTeam().getId();
					XmlTeam xmlTeamAway = XmlTeamDao.INSTANCE.findById(connection, awayTeamId);
					if(xmlTeamAway == null){
						xmlTeamAway = new XmlTeam();
						xmlTeamAway.setId(awayTeamId);
						xmlTeamAway.setName(matchInfo.getAwayTeam().getName().getTranslation("en"));
						xmlTeamAway.setSportId(matchInfo.getSport().getId());
						xmlTeamAway.setCreatedAt(now.toDate());
						xmlTeamAway.setUpdatedAt(now.toDate());
						XmlTeamDao.INSTANCE.insert(connection, xmlTeamAway);
					}

					//category to xml_country
					XmlCountry xmlCountry = XmlCountryDao.INSTANCE.findById(connection, matchInfo.getCategory().getId());
					if(xmlCountry == null){
						xmlCountry = new XmlCountry();
						xmlCountry.setId(matchInfo.getCategory().getId());
						xmlCountry.setName(matchInfo.getCategory().getName().getTranslation("en"));
						xmlCountry.setSportId(matchInfo.getSport().getId());
						xmlCountry.setCreatedAt(now.toDate());
						xmlCountry.setUpdatedAt(now.toDate());
						XmlCountryDao.INSTANCE.insert(connection, xmlCountry);
					}

					//tournament to xml_league
					XmlLeague xmlLeague = XmlLeagueDao.INSTANCE.findById(connection, matchInfo.getTournament().getId());
					if(xmlLeague == null){
						xmlLeague = new XmlLeague();
						xmlLeague.setId(matchInfo.getTournament().getId());
						xmlLeague.setName(matchInfo.getTournament().getName().getTranslation("en"));
						xmlLeague.setLocationId(matchInfo.getCategory().getId());
						xmlLeague.setSportId(matchInfo.getSport().getId());
						xmlLeague.setType("tournament");
						xmlLeague.setCreatedAt(now.toDate());
						xmlLeague.setUpdatedAt(now.toDate());
						XmlLeagueDao.INSTANCE.insert(connection, xmlLeague);
					}

					//event to xml_event
					xmlEvent = new XmlEvent();
					xmlEvent.setId(eventId);
					xmlEvent.setStartDate(matchInfo.getDateOfMatch().toDate());
					xmlEvent.setSportId(matchInfo.getSport().getId());
					xmlEvent.setLeagueId(matchInfo.getTournament().getId());
					xmlEvent.setLocationId(matchInfo.getCategory().getId());
					xmlEvent.setHomeTeamId(homeTeamId);
					xmlEvent.setAwayTeamId(awayTeamId);
					xmlEvent.setName("");
					xmlEvent.setType("tournament");
					xmlEvent.setCreatedAt(now.toDate());
					xmlEvent.setUpdatedAt(now.toDate());
					XmlEventDao.INSTANCE.insert(connection, xmlEvent);


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

		}).start();
	}
}
