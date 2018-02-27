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
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.iceland.betradar.dao.Dao;
import com.iceland.betradar.dao.IcelandDataSource;

public enum XmlOutComeDao implements Dao<XmlOutCome> {

	INSTANCE;
	private ResultSetHandler<XmlOutCome> resultSetHandler;
	private ResultSetHandler<List<XmlOutCome>> resultSetListHandler;

	XmlOutComeDao() {
		Map<String, String> columnMap = new HashMap<>();
		columnMap.put("id", "id");
		columnMap.put("eventID", "eventId");
		columnMap.put("oddsType", "oddsType");
		columnMap.put("outcome", "outcome");
		columnMap.put("line", "line");
		columnMap.put("group_name", "groupName");
		columnMap.put("group_order", "groupOrder");
		columnMap.put("value", "value");
		columnMap.put("lord", "lord");
		columnMap.put("asya", "asya");
		columnMap.put("created_at", "createdAt");
		columnMap.put("updated_at", "updatedAt");
		BeanProcessor bs = new BeanProcessor(columnMap);
		resultSetHandler = new BeanHandler<>(XmlOutCome.class, new BasicRowProcessor(bs));
		resultSetListHandler = new BeanListHandler<XmlOutCome>(XmlOutCome.class,new BasicRowProcessor(bs));
	}

	@Override
	public XmlOutCome findById(Connection connection, Long id) throws SQLException {
		String sql = "select * from xml_outcome where id = ?";
		return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, id);
	}

	public List<XmlOutCome> findByEventId(Connection connection, Long eventId) throws SQLException {
		String sql = "select * from xml_outcome where eventID = ?";
		return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetListHandler, eventId);
	}

	public XmlOutCome findByEventIdOddsTypeOutComeLine(Connection connection, Long eventId, Long oddsType, String outCome, String line) throws SQLException {
		String sql = "select * from xml_outcome where eventID = ? and oddsType = ?  and outcome = ? and line = ?";
		return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, eventId, oddsType, outCome, line);
	}

	public XmlOutCome findByEventIdOddsTypeOutCome(Connection connection, Long eventId, Long oddsType, String outCome) throws SQLException {
		String sql = "select * from xml_outcome where eventID = ? and oddsType = ?  and outcome = ? ";
		return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, eventId, oddsType, outCome);
	}

	public Integer deleteById(Connection connection, Long eventId) throws SQLException {
		String sql = "delete from xml_outcome where eventID = ?";
		return IcelandDataSource.INSTANCE.run().update(connection, sql, eventId);
	}

	public int[] insert(Connection connection, List<XmlOutCome> outComeList) throws SQLException {
		String sql = new StringBuilder().append("INSERT INTO xml_outcome( ")
				.append("  eventID ")
				.append("  ,oddsType ")
				.append("  ,outcome ")
				.append("  ,line ")
				.append("  ,group_name ")
				.append("  ,group_order ")
				.append("  ,value ")
				.append("  ,lord ")
				.append("  ,asya ")
				.append("  ,created_at ")
				.append("  ,updated_at ")
				.append(") VALUES ( ")
				.append("   ? ")
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
				.append(" ) ")
				.toString();
		Object[][] params = new Object[outComeList.size()][11];
		int count = 0;
		for(XmlOutCome xmlOutCome : outComeList) {
			params[count][0] = xmlOutCome.getEventId();
			params[count][1] = xmlOutCome.getOddsType();
			params[count][2] = xmlOutCome.getOutcome();
			params[count][3] = xmlOutCome.getLine();
			params[count][4] = xmlOutCome.getGroupName();
			params[count][5] = xmlOutCome.getGroupOrder();
			params[count][6] = xmlOutCome.getValue();
			params[count][7] = xmlOutCome.getLord();
			params[count][8] = xmlOutCome.getAsya();
			params[count][9] = xmlOutCome.getCreatedAt();
			params[count][10] = xmlOutCome.getUpdatedAt();
			count += 1;
		}
		return IcelandDataSource.INSTANCE.run().batch(connection,sql,params);

	}

	@Override
	public Integer updateById(Connection connection, XmlOutCome xmlOutCome) throws SQLException {
		String sql = new StringBuilder().append("UPDATE xml_outcome ")
				.append(" SET ")
				.append("  eventID = ? ")
				.append("  ,oddsType = ? ")
				.append("  ,outcome = ? ")
				.append("  ,line = ? ")
				.append("  ,group_name = ? ")
				.append("  ,group_order = ? ")
				.append("  ,value = ? ")
				.append("  ,lord = ? ")
				.append("  ,asya = ? ")
				.append("  ,created_at = ? ")
				.append("  ,updated_at = ? ")
				.append(" WHERE id = ? ")
				.toString();
		return IcelandDataSource.INSTANCE.run()
				.update(connection, sql, xmlOutCome.getEventId(), xmlOutCome.getOddsType(), xmlOutCome.getOutcome(), xmlOutCome.getLine(), xmlOutCome.getGroupName(), xmlOutCome.getGroupOrder(),
						xmlOutCome.getValue(), xmlOutCome.getLord(), xmlOutCome.getAsya(), xmlOutCome.getCreatedAt(), xmlOutCome.getUpdatedAt(), xmlOutCome.getId());
	}

	@Override
	public Integer insert(Connection connection, XmlOutCome xmlOutCome) throws SQLException {
		String sql = new StringBuilder().append("INSERT INTO xml_outcome( ")
				.append("   eventID ")
				.append("  ,oddsType ")
				.append("  ,outcome ")
				.append("  ,line ")
				.append("  ,group_name ")
				.append("  ,group_order ")
				.append("  ,value ")
				.append("  ,lord ")
				.append("  ,asya ")
				.append("  ,created_at ")
				.append("  ,updated_at ")
				.append(") VALUES (")
				.append("   ? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,?  ")
				.append("  ,?  ")
				.append("  ,?  ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append("  ,? ")
				.append(")")
				.toString();

		return IcelandDataSource.INSTANCE.run()
				.update(connection, sql, xmlOutCome.getEventId(), xmlOutCome.getOddsType(), xmlOutCome.getOutcome(), xmlOutCome.getLine(),xmlOutCome.getGroupName(),
						xmlOutCome.getGroupOrder(), xmlOutCome.getValue(), xmlOutCome.getLord(), xmlOutCome.getAsya(),
						xmlOutCome.getCreatedAt(), xmlOutCome.getUpdatedAt());
	}

	public int updateByEventIdSetValueToZero(Connection connection, Long eventId) throws SQLException {
		String sql = "UPDATE xml_outcome set value = 0 where eventID = ?";
		return IcelandDataSource.INSTANCE.run()
				.update(connection, sql, eventId);
	}

	public int deleteByEventIdAndValue(Connection connection, Long eventId, Double value) throws SQLException {
		String sql = "DELETE from xml_outcome where eventID = ? and value = ?";
		return IcelandDataSource.INSTANCE.run().update(connection, sql, eventId,value);
	}

}
