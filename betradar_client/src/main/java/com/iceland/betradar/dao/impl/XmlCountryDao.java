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

public enum XmlCountryDao implements Dao<XmlCountry>{
	INSTANCE;

	private ResultSetHandler<XmlCountry> resultSetHandler;

	XmlCountryDao() {
		Map<String, String> columnMap = new HashMap<>();
		columnMap.put("id", "id");
		columnMap.put("name", "name");
		columnMap.put("sportID", "sportId");
		columnMap.put("created_at", "createdAt");
		columnMap.put("updated_at", "updatedAt");
		BeanProcessor bs = new BeanProcessor(columnMap);
		resultSetHandler = new BeanHandler<>(XmlCountry.class, new BasicRowProcessor(bs));
	}


	@Override
	public XmlCountry findById(Connection connection, Long id) throws SQLException {
		String sql = "select * from xml_country where id = ?";
		return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, id);
	}

	@Override
	public Integer updateById(Connection connection,XmlCountry xmlCountry) throws SQLException {
		String sql = new StringBuilder()
				.append("UPDATE xml_country ")
				.append("SET ")
				.append("  name = ? ")
				.append("  ,sportID = ? ")
				.append("  ,created_at = ? ")
				.append("  ,updated_at = ? ")
				.append("WHERE id = ? ")
				.toString();
		return IcelandDataSource.INSTANCE.run()
				.update(connection, sql, xmlCountry.getName(), xmlCountry.getSportId(),
						xmlCountry.getCreatedAt(), xmlCountry.getUpdatedAt(), xmlCountry.getId());
	}

	@Override
	public Integer insert(Connection connection, XmlCountry xmlCountry) throws SQLException {
		String sql = new StringBuilder()
				.append("INSERT INTO xml_country(")
				.append("   id")
				.append("  ,name")
				.append("  ,sportID")
				//.append("  ,status")
				//.append("  ,order_id")
				.append("  ,created_at")
				.append("  ,updated_at")
				.append(") VALUES (")
				.append("   ?")
				.append("  ,?")
				.append("  ,?")
				//.append("  ,?")
				//.append("  ,?")
				.append("  ,?")
				.append("  ,?")
				.append(")")
				.toString();
		return IcelandDataSource.INSTANCE.run()
				.update(connection, sql, xmlCountry.getId(), xmlCountry.getName(), xmlCountry.getSportId(), xmlCountry.getCreatedAt(), xmlCountry.getUpdatedAt());
	}

}
