package com.iceland.betradar.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.iceland.betradar.dao.Dao;
import com.iceland.betradar.dao.IcelandDataSource;

public enum XmlLeagueDao implements Dao<XmlLeague> {

	INSTANCE;
	private ResultSetHandler<XmlLeague> resultSetHandler;

	XmlLeagueDao() {
		Map<String, String> columnMap = new HashMap<>();
		columnMap.put("id", "id");
		columnMap.put("name", "name");
		columnMap.put("locationID", "locationId");
		columnMap.put("sportID", "sportId");
		columnMap.put("min_event", "minEvent");
		columnMap.put("won_limit", "wonLimit");
		columnMap.put("customer_limit", "customerLimit");
		columnMap.put("profit", "profit");
		columnMap.put("type", "type");
		columnMap.put("time", "time");
		columnMap.put("created_at", "createdAt");
		columnMap.put("updated_at", "updatedAt");
		BeanProcessor bs = new BeanProcessor(columnMap);
		resultSetHandler = new BeanHandler<>(XmlLeague.class, new BasicRowProcessor(bs));
	}

	@Override
	public XmlLeague findById(Connection connection, Long id) throws SQLException {
		String sql = "select * from xml_league where id = ?";
		return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, id);
	}

	public XmlLeague findByName(Connection connection, String name) throws SQLException {
		String sql = "select * from xml_league where name = ?";
		return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, name);
	}

	@Override
	public Integer updateById(Connection connection, XmlLeague xmlLeague) throws SQLException {
		String sql = new StringBuilder().append("UPDATE xml_league ")
				.append("SET ")
				.append("  name = ? ")
				.append("  ,locationID = ? ")
				.append("  ,sportID = ? ")
				.append("  ,min_event = ? ")
				.append("  ,won_limit = ? ")
				.append("  ,customer_limit = ? ")
				.append("  ,profit = ? ")
				.append("  ,type = ? ")
				.append("  ,created_at = ? ")
				.append("  ,updated_at = ? ")
				.append("WHERE id = ? ")
				.toString();

		return IcelandDataSource.INSTANCE.run()
				.update(connection, sql, xmlLeague.getName(), xmlLeague.getLocationId(), xmlLeague.getSportId(), xmlLeague.getMinEvent(), xmlLeague.getWonLimit(), xmlLeague.getCustomerLimit(),
						xmlLeague.getProfit(), xmlLeague.getType(), xmlLeague.getCreatedAt(), xmlLeague.getUpdatedAt(),
						xmlLeague.getId());
	}

	@Override
	public Integer insert(Connection connection, XmlLeague xmlLeague) throws SQLException {
		String sql = new StringBuilder().append(" INSERT INTO xml_league( ")
				.append("   id ")
				.append("  ,name ")
				.append("  ,locationID ")
				.append("  ,sportID ")
				.append("  ,type ")
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
				.append(") ")
				.toString();

		return IcelandDataSource.INSTANCE.run()
				.update(connection, sql, xmlLeague.getId(), xmlLeague.getName(), xmlLeague.getLocationId(), xmlLeague.getSportId(), xmlLeague.getType(),
						xmlLeague.getCreatedAt(), xmlLeague.getUpdatedAt());
	}
}
