package com.iceland.betradar.service.persist.live;

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
import com.sportradar.sdk.feed.liveodds.entities.common.BetClearRollbackEntity;

public enum LiveBetResultsRollBackPersist {
	INSTANCE;
	private final static Logger logger = LoggerFactory.getLogger(LiveBetResultsRollBackPersist.class);

	private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

	public void persistBetClearResultsIntoDb(BetClearRollbackEntity entity) {
		singleThreadExecutor.submit(() -> {
			Connection connection = null;
			try{
				connection = IcelandDataSource.INSTANCE.getDs().getConnection();
				DateTime now = DateTime.now();
				BetClearResult betClearResult = new BetClearResult();
				betClearResult.setEventId(entity.getEventHeader().getEventId());
				betClearResult.setLineId(entity.getEventOdds().get(0).getId());
				betClearResult.setDetail(JSON.INSTANCE.gson.toJson(entity));
				betClearResult.setType("live");
				betClearResult.setClearType("rollback");
				betClearResult.setIsSet("no");
				betClearResult.setCreatedAt(now.toDate());
				betClearResult.setUpdatedAt(now.toDate());
				BetClearResultDao.INSTANCE.insert(connection, betClearResult);
			} catch (SQLException e){
				logger.error("DB UPDATE FAILED SINGLE INSERT BET CLEAR ROLLBACK RESULTS SQL EXCEPTION", e);
				DbUtils.rollbackAndCloseQuietly(connection);
			} catch (Exception e){
				logger.error("DB UPDATE FAILED SINGLE INSERT BET CLEAR ROLLBACK RESULTS GENERAL EXCEPTION", e);
				DbUtils.rollbackAndCloseQuietly(connection);
			} finally{
				DbUtils.commitAndCloseQuietly(connection);
			}
		});
	}
}
