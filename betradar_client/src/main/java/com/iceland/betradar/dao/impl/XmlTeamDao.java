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

public enum XmlTeamDao implements Dao<XmlTeam> {
	INSTANCE;
	private ResultSetHandler<XmlTeam> resultSetHandler;

	XmlTeamDao() {
		Map<String, String> columnMap = new HashMap<>();
		columnMap.put("id", "id");
		columnMap.put("name", "name");
		columnMap.put("superID", "superId");
		columnMap.put("sportID", "sportId");
		columnMap.put("created_at", "createdAt");
		columnMap.put("updated_at", "updatedAt");
		BeanProcessor bs = new BeanProcessor(columnMap);
		resultSetHandler = new BeanHandler<>(XmlTeam.class, new BasicRowProcessor(bs));
	}

	@Override
	public XmlTeam findById(Connection connection, Long id) throws SQLException {
		String sql = "select * from xml_team where id = ?";
		return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, id);
	}

	@Override
	public Integer updateById(Connection connection, XmlTeam xmlTeam) throws SQLException {
		String sql = new StringBuilder().append("UPDATE xml_team ")
				.append("SET ")
				.append("  name = ? ")
				.append("  ,superID = ? ")
				.append("  ,sportID = ? ")
				.append("  ,created_at = ? ")
				.append("  ,updated_at = ? ")
				.append("WHERE id = ? ")
				.toString();
		return IcelandDataSource.INSTANCE.run().update(connection, sql, xmlTeam.getName(), xmlTeam.getSuperId(), xmlTeam.getSportId(), xmlTeam.getCreatedAt(), xmlTeam.getUpdatedAt(), xmlTeam.getId());
	}

	@Override
	public Integer insert(Connection connection, XmlTeam xmlTeam) throws SQLException {
		String sql = new StringBuilder().append("INSERT INTO xml_team(")
				.append("   id")
				.append("  ,name")
				.append("  ,superID")
				.append("  ,sportID")
				.append("  ,created_at")
				.append("  ,updated_at")
				.append(") VALUES (")
				.append("   ? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append(")")
				.toString();
		return IcelandDataSource.INSTANCE.run().update(connection, sql,xmlTeam.getId(), xmlTeam.getName(), xmlTeam.getSuperId(), xmlTeam.getSportId(), xmlTeam.getCreatedAt(), xmlTeam.getUpdatedAt());
	}
}
