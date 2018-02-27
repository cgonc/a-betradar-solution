package com.iceland.betradar.service.persist.sites;

import com.iceland.betradar.dao.impl.EventBetProfit;
import com.iceland.betradar.dao.impl.EventBetProfitDao;
import com.iceland.betradar.dao.impl.LeagueBetProfit;
import com.iceland.betradar.dao.impl.LeagueBetProfitDao;
import com.iceland.betradar.dao.impl.XmlBetType;
import com.iceland.betradar.dao.impl.XmlBetTypeDao;
import com.iceland.betradar.dao.impl.XmlLeague;
import com.iceland.betradar.dao.impl.XmlLeagueDao;
import com.iceland.betradar.dao.impl.XmlLimit;
import com.iceland.betradar.dao.impl.XmlLimitDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public enum SiteCalculations {
    INSTANCE;
    private final Logger logger = LoggerFactory.getLogger("analytics");

    public Double calculateSiteProfitValue(Long eventId, Long leagueId, long sideId,  Long sportId, long siteId, Connection connection) throws SQLException {
        Double siteProfitValue = 100d;
        logger.info("----------------------------------Starts----------------------------------");
        logger.info("Site calculations has been started for eventId :{} leagueId:{} sideId:{} siteId:{} sportId:{}", eventId, leagueId, sideId, siteId, sportId);
        EventBetProfit eventBetProfit = EventBetProfitDao.INSTANCE.findByEventIdAndSideIdAndSiteId(connection, eventId, sideId, siteId);

        if (eventBetProfit != null) {
            logger.info("EventBetProfit found returned profit : {}" , eventBetProfit.getProfit());
            logger.info("----------------------------------Ends----------------------------------");
            return eventBetProfit.getProfit();
        }

        LeagueBetProfit leagueBetProfit = LeagueBetProfitDao.INSTANCE.findByLeagueIdAndSideIdAndSiteId(connection, leagueId, sideId, siteId);
        if (leagueBetProfit != null) {
            logger.info("LeagueBetProfit found returned profit : {}" , leagueBetProfit.getProfit());
            logger.info("----------------------------------Ends----------------------------------");
            return leagueBetProfit.getProfit();
        }

        XmlLimit xmlLimitForLeague = XmlLimitDao.INSTANCE.findByTypeAndTypeIdAndSiteId(connection, "xml_league", leagueId, siteId);
        if (xmlLimitForLeague != null) {
            logger.info("XmlLimit found returned profit : {}" , xmlLimitForLeague.getProfit());
            logger.info("----------------------------------Ends----------------------------------");
            return xmlLimitForLeague.getProfit();
        }

        XmlLeague xmlLeague = XmlLeagueDao.INSTANCE.findById(connection, leagueId);
        if (xmlLeague != null && xmlLeague.getProfit() > 0d) {
            logger.info("XmlLeague found returned profit : {}" , xmlLeague.getProfit());
            logger.info("----------------------------------Ends----------------------------------");
            return xmlLeague.getProfit();
        }

        XmlBetType xmlBetType = XmlBetTypeDao.INSTANCE.findBySportIdAndSideId(connection, sportId, sideId);
        if (xmlBetType != null) {
            XmlLimit xmlLimitForBetType = XmlLimitDao.INSTANCE.findByTypeAndTypeIdAndSiteId(connection, "xml_bet_type", xmlBetType.getId(), siteId);
            if (xmlLimitForBetType != null) {
                logger.info("XmlLimit found returned profit : {}" , xmlLimitForBetType.getProfit());
                logger.info("----------------------------------Ends----------------------------------");
                return xmlLimitForBetType.getProfit();
            } else {
                logger.info("XmlBetType found returned profit : {}" , xmlBetType.getProfit());
                logger.info("----------------------------------Ends----------------------------------");
                return xmlBetType.getProfit();
            }
        }
        logger.info("----------------------------------NOTHING HAS FOUND RETURNING DEFAULT 100----------------------------------");
        logger.info("----------------------------------Göt Baran Ends----------------------------------");
        return siteProfitValue;
    }
}
