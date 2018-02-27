package com.iceland.betradar.service.persist.live;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.dbutils.DbUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iceland.betradar.dao.IcelandDataSource;
import com.iceland.betradar.dao.impl.BetClearResult;
import com.iceland.betradar.dao.impl.BetClearResultDao;
import com.iceland.betradar.service.JSON;
import com.sportradar.sdk.feed.liveodds.entities.common.BetClearEntity;

public enum LiveBetResultsNormalPersist {
	INSTANCE;
	private final static Logger logger = LoggerFactory.getLogger(LiveBetResultsNormalPersist.class);

	private Queue<BetClearEntity> betClearEntityQueue = new ConcurrentLinkedDeque<>();
	private ExecutorService bulkInsertExecutor = Executors.newSingleThreadExecutor();
	private ExecutorService singleInsertExecutor = Executors.newFixedThreadPool(10);
	private AtomicBoolean isBulkBeingInsertedToDb = new AtomicBoolean(false);

	public void persistBetClearResultsIntoDb(BetClearEntity entity) {
		if(betClearEntityQueue.size() >= 100){
			isBulkBeingInsertedToDb.set(true);
			List<BetClearResult> betClearEntities = new ArrayList<>();
			DateTime now = DateTime.now();
			betClearEntityQueue.forEach(betClearEntity -> {
				BetClearResult betClearResult = new BetClearResult();
				betClearResult.setEventId(betClearEntity.getEventHeader().getEventId());
				betClearResult.setLineId(betClearEntity.getEventOdds().get(0).getId());
				betClearResult.setDetail(JSON.INSTANCE.gson.toJson(betClearEntity));
				betClearResult.setType("live");
				betClearResult.setClearType("normal");
				betClearResult.setIsSet("no");
				betClearResult.setCreatedAt(now.toDate());
				betClearResult.setUpdatedAt(now.toDate());
				betClearEntities.add(betClearResult);
			});

			bulkInsertExecutor.submit(() -> {
				Connection connection = null;
				try{
					connection = IcelandDataSource.INSTANCE.getDs().getConnection();
					BetClearResultDao.INSTANCE.insert(connection, betClearEntities);
				} catch (SQLException e){
					logger.error("DB UPDATE FAILED BULK INSERT BET CLEAR RESULTS SQL EXCEPTION", e);
					DbUtils.rollbackAndCloseQuietly(connection);
				} catch (Exception e){
					logger.error("DB UPDATE FAILED BULK INSERT BET CLEAR RESULTS GENERAL EXCEPTION", e);
					DbUtils.rollbackAndCloseQuietly(connection);
				} finally{
					DbUtils.commitAndCloseQuietly(connection);
				}

				isBulkBeingInsertedToDb.set(false);

			});
			betClearEntityQueue.clear();
			return;
		}

		if(isBulkBeingInsertedToDb.get()){
			//This time just insert the incoming bets.
			singleInsertExecutor.submit(() -> {
				Connection connection = null;
				try{
					connection = IcelandDataSource.INSTANCE.getDs().getConnection();
					DateTime now = DateTime.now();
					BetClearResult betClearResult = new BetClearResult();
					betClearResult.setEventId(entity.getEventHeader().getEventId());
					betClearResult.setLineId(entity.getEventOdds().get(0).getId());
					betClearResult.setDetail(JSON.INSTANCE.gson.toJson(entity));
					betClearResult.setType("live");
					betClearResult.setClearType("normal");
					betClearResult.setIsSet("no");
					betClearResult.setCreatedAt(now.toDate());
					betClearResult.setUpdatedAt(now.toDate());
					BetClearResultDao.INSTANCE.insert(connection, betClearResult);
				} catch (SQLException e){
					logger.error("DB UPDATE FAILED SINGLE INSERT BET CLEAR RESULTS SQL EXCEPTION", e);
					DbUtils.rollbackAndCloseQuietly(connection);
				} catch (Exception e){
					logger.error("DB UPDATE FAILED SINGLE INSERT BET CLEAR RESULTS GENERAL EXCEPTION", e);
					DbUtils.rollbackAndCloseQuietly(connection);
				} finally{
					DbUtils.commitAndCloseQuietly(connection);
				}
			});
		} else {
			//This time just populate the queue...
			betClearEntityQueue.add(entity);
		}
	}
}
