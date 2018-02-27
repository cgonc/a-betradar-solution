package com.iceland.betradar.service.persist.lcoo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.dbutils.DbUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iceland.betradar.dao.IcelandDataSource;
import com.iceland.betradar.dao.impl.BetClearResult;
import com.iceland.betradar.dao.impl.BetClearResultDao;
import com.iceland.betradar.service.JSON;
import com.sportradar.sdk.feed.lcoo.entities.MatchEntity;

public enum LcooMatchResultsPersist {
	INSTANCE;
	private final static Logger logger = LoggerFactory.getLogger(LcooMatchResultsPersist.class);
	private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

	public void persistMatchResults(MatchEntity match) {
		singleThreadExecutor.submit(() -> {
			Connection connection = null;
			try{
				connection = IcelandDataSource.INSTANCE.getDs().getConnection();
				DateTime now = DateTime.now();
				BetClearResult betClearResult = new BetClearResult();
				betClearResult.setEventId(match.getMatchId());
				betClearResult.setLineId(match.getMatchId());
				betClearResult.setDetail(JSON.INSTANCE.gson.toJson(match));
				betClearResult.setType("prelive");
				betClearResult.setClearType("normal");
				betClearResult.setIsSet("no");
				betClearResult.setCreatedAt(now.toDate());
				betClearResult.setUpdatedAt(now.toDate());
				BetClearResultDao.INSTANCE.insert(connection, betClearResult);
			} catch (SQLException e){
				logger.error("DB UPDATE FAILED LCOO MATCH INFO WITH RESULTS", e);
				DbUtils.rollbackAndCloseQuietly(connection);
			} catch (Exception e){
				logger.error("DB UPDATE FAILED LCOO MATCH INFO WITH RESULTS", e);
				DbUtils.rollbackAndCloseQuietly(connection);
			} finally{
				DbUtils.commitAndCloseQuietly(connection);
			}
		});
	}
}
