package com.iceland.betradar.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.iceland.betradar.dao.Dao;
import com.iceland.betradar.dao.IcelandDataSource;

public enum BetClearResultDao implements Dao<BetClearResult> {
	INSTANCE;

	private ResultSetHandler<BetClearResult> resultSetHandler;

	BetClearResultDao() {
		Map<String, String> columnMap = new HashMap<>();
		columnMap.put("id", "id");
		columnMap.put("eventID", "eventId");
		columnMap.put("lineID", "lineId");
		columnMap.put("detail", "detail");
		columnMap.put("type", "type");
		columnMap.put("cleartype", "clearType");
		columnMap.put("is_set", "isSet");
		columnMap.put("created_at", "createdAt");
		columnMap.put("updated_at", "updatedAt");
		BeanProcessor bs = new BeanProcessor(columnMap);
		resultSetHandler = new BeanHandler<>(BetClearResult.class, new BasicRowProcessor(bs));
	}

	@Override
	public BetClearResult findById(Connection connection, Long id) throws SQLException {
		return null;
	}

	@Override
	public Integer updateById(Connection connection, BetClearResult betClearResult) throws SQLException {
		return null;
	}

	@Override
	public Integer insert(Connection connection, BetClearResult betClearResult) throws SQLException {
		String sql = new StringBuilder().append("INSERT INTO bet_clear_result( ")
				.append("   eventID ")
				.append("  ,lineID ")
				.append("  ,detail ")
				.append("  ,type ")
				.append("  ,cleartype ")
				.append("  ,is_set ")
				.append("  ,created_at ")
				.append("  ,updated_at ")
				.append(") VALUES (")
				.append("   ? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append(")")
				.toString();
		return IcelandDataSource.INSTANCE.run()
				.update(connection, sql, betClearResult.getEventId(), betClearResult.getLineId(), betClearResult.getDetail(), betClearResult.getType(), betClearResult.getClearType(), betClearResult.getIsSet(), betClearResult.getCreatedAt(),
						betClearResult.getUpdatedAt());
	}

	public int[] insert(Connection connection, List<BetClearResult> betClearResults) throws SQLException {
		String sql = new StringBuilder().append("INSERT INTO bet_clear_result( ")
				.append("   eventID ")
				.append("  ,lineID ")
				.append("  ,detail ")
				.append("  ,type ")
				.append("  ,cleartype ")
				.append("  ,is_set ")
				.append("  ,created_at ")
				.append("  ,updated_at ")
				.append(") VALUES (")
				.append("   ? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append(")")
				.toString();
		Object[][] params = new Object[betClearResults.size()][8];
		int count = 0;
		for(BetClearResult betClearResult : betClearResults){
			params[count][0] = betClearResult.getEventId();
			params[count][1] = betClearResult.getLineId();
			params[count][2] = betClearResult.getDetail();
			params[count][3] = betClearResult.getType();
			params[count][4] = betClearResult.getClearType();
			params[count][5] = betClearResult.getIsSet();
			params[count][6] = betClearResult.getCreatedAt();
			params[count][7] = betClearResult.getUpdatedAt();
			count += 1;
		}
		return IcelandDataSource.INSTANCE.run().batch(connection, sql, params);
	}
}
