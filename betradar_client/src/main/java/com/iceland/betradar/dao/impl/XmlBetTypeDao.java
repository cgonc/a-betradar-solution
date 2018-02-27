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

public enum XmlBetTypeDao implements Dao<XmlBetType> {
	INSTANCE;

	private ResultSetHandler<XmlBetType> resultSetHandler;

	XmlBetTypeDao() {
		Map<String, String> columnMap = new HashMap<>();
		columnMap.put("id", "id");
		columnMap.put("sideID", "sideId");
		columnMap.put("name", "name");
		columnMap.put("sportID", "sportId");
		columnMap.put("maxlimit", "maxLimit");
		columnMap.put("profit", "profit");
		columnMap.put("created_at", "createdAt");
		columnMap.put("updated_at", "updatedAt");
		BeanProcessor bs = new BeanProcessor(columnMap);
		resultSetHandler = new BeanHandler<>(XmlBetType.class, new BasicRowProcessor(bs));
	}

	@Override
	public XmlBetType findById(Connection connection, Long id) throws SQLException {
		String sql = "select * from xml_bet_type where id = ?";
		return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, id);
	}

	public XmlBetType findBySportIdAndSideId(Connection connection, Long sportId, Long sideId) throws SQLException {
		String sql = "select * from xml_bet_type where sportID = ? and sideID = ? ";
		return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, sportId, sideId);
	}

	@Override
	public Integer updateById(Connection connection, XmlBetType xmlBetType) throws SQLException {
		String sql = new StringBuilder().append(" UPDATE xml_bet_type ")
				.append(" SET ")
				.append("   sideID = ? ")
				.append("  ,name = ? ")
				.append("  ,sportID = ? ")
				.append("  ,maxlimit = ? ")
				.append("  ,profit = ? ")
				.append("  ,created_at = ? ")
				.append("  ,updated_at = ? ")
				.append(" WHERE id = ? ")
				.toString();

		return IcelandDataSource.INSTANCE.run()
				.update(connection, sql, xmlBetType.getSideId(), xmlBetType.getName(), xmlBetType.getSportId(), xmlBetType.getMaxLimit(), xmlBetType.getProfit(),
						xmlBetType.getCreatedAt(), xmlBetType.getUpdatedAt(), xmlBetType.getId());
	}

	@Override
	public Integer insert(Connection connection, XmlBetType xmlBetType) throws SQLException {
		String sql = new StringBuilder().append("INSERT INTO xml_bet_type(")
				.append("   sideID")
				.append("  ,sportID")
				.append("  ,created_at")
				.append("  ,updated_at")
				.append(") VALUES (")
				.append("   ? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,?  ")
				.append(")")
				.toString();

		return IcelandDataSource.INSTANCE.run()
				.update(connection, sql, xmlBetType.getSideId(), xmlBetType.getSportId(),
						 xmlBetType.getCreatedAt(), xmlBetType.getUpdatedAt());
	}
}
