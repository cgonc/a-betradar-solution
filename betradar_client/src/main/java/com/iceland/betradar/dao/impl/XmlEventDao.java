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

public enum XmlEventDao implements Dao<XmlEvent> {
	INSTANCE;
	private ResultSetHandler<XmlEvent> resultSetHandler;

	XmlEventDao() {
		Map<String, String> columnMap = new HashMap<>();
		columnMap.put("id", "id");
		columnMap.put("startDate", "startDate");
		columnMap.put("sportID", "sportId");
		columnMap.put("leagueID", "leagueId");
		columnMap.put("locationID", "locationId");
		columnMap.put("homeTeamID", "homeTeamId");
		columnMap.put("awayTeamID", "awayTeamId");
		columnMap.put("name", "name");
		columnMap.put("time", "time");
		columnMap.put("type", "type");
		columnMap.put("created_at", "createdAt");
		columnMap.put("updated_at", "updatedAt");
		BeanProcessor bs = new BeanProcessor(columnMap);
		resultSetHandler = new BeanHandler<>(XmlEvent.class, new BasicRowProcessor(bs));
	}

	@Override
	public XmlEvent findById(Connection connection, Long id) throws SQLException {
		String sql = "select * from xml_event where id = ?";
		return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, id);
	}

	@Override
	public Integer updateById(Connection connection, XmlEvent xmlEvent) throws SQLException {
		String sql = new StringBuilder().append("UPDATE xml_event ")
				.append("SET ")
				.append("   startDate = ? ")
				.append("  ,sportID = ? ")
				.append("  ,leagueID = ? ")
				.append("  ,locationID = ? ")
				.append("  ,homeTeamID = ? ")
				.append("  ,awayTeamID = ? ")
				.append("  ,name = ? ")
				.append("  ,`time` = ? ")
				.append("  ,type = ? ")
				.append("  ,created_at = ? ")
				.append("  ,updated_at = ? ")
				.append("WHERE id = ? ")
				.toString();
		return IcelandDataSource.INSTANCE.run()
				.update(connection, sql, xmlEvent.getStartDate(), xmlEvent.getSportId(), xmlEvent.getLeagueId(), xmlEvent.getLocationId(), xmlEvent.getHomeTeamId(), xmlEvent.getAwayTeamId(),
                        xmlEvent.getName(), xmlEvent.getTime(), xmlEvent.getType(),xmlEvent.getCreatedAt(), xmlEvent.getUpdatedAt(),xmlEvent.getId());
	}

	@Override
	public Integer insert(Connection connection, XmlEvent xmlEvent) throws SQLException {
		String sql = new StringBuilder().append("INSERT INTO xml_event( ")
				.append("   id ")
				.append("  ,startDate ")
				.append("  ,sportID ")
				.append("  ,leagueID ")
				.append("  ,locationID ")
				.append("  ,homeTeamID ")
				.append("  ,awayTeamID ")
				.append("  ,name ")
				.append("  ,`time` ")
				.append("  ,type ")
				.append("  ,created_at ")
				.append("  ,updated_at ")
				.append(") VALUES ( ")
				.append("  ? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append(") ")
				.toString();

		return IcelandDataSource.INSTANCE.run()
				.update(connection, sql, xmlEvent.getId(), xmlEvent.getStartDate(), xmlEvent.getSportId(), xmlEvent.getLeagueId(), xmlEvent.getLocationId(), xmlEvent.getHomeTeamId(),
						xmlEvent.getAwayTeamId(), xmlEvent.getName(), xmlEvent.getTime(), xmlEvent.getType(), xmlEvent.getCreatedAt(), xmlEvent.getUpdatedAt());
	}
}
