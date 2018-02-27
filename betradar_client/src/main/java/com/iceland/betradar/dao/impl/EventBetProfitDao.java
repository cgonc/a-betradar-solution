package com.iceland.betradar.dao.impl;

import com.iceland.betradar.dao.Dao;
import com.iceland.betradar.dao.IcelandDataSource;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public enum EventBetProfitDao implements Dao<EventBetProfit> {
    INSTANCE;

    private ResultSetHandler<EventBetProfit> resultSetHandler;

    EventBetProfitDao() {
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("id", "id");
        columnMap.put("site_id", "siteId");
        columnMap.put("eventID", "eventId");
        columnMap.put("sideID", "sideId");
        columnMap.put("profit", "profit");
        columnMap.put("created_at", "createdAt");
        columnMap.put("updated_at", "updatedAt");
        BeanProcessor bs = new BeanProcessor(columnMap);
        resultSetHandler = new BeanHandler<>(EventBetProfit.class, new BasicRowProcessor(bs));
    }

    @Override
    public EventBetProfit findById(Connection connection, Long id) throws SQLException {
        String sql = "select * from eventbet_profit where id = ?";
        return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, id);
    }

    public EventBetProfit findByEventIdAndSideIdAndSiteId(Connection connection, Long eventId, Long sideId, Long siteId) throws SQLException {
        String sql = "select * from eventbet_profit where eventID = ? and sideID = ? and site_id = ?";
        return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, eventId, sideId ,siteId);
    }

    @Override
    public Integer updateById(Connection connection, EventBetProfit eventBetProfit) throws SQLException {
        return null;
    }

    @Override
    public Integer insert(Connection connection, EventBetProfit eventBetProfit) throws SQLException {
        return null;
    }
}
