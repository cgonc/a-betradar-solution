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

public enum LeagueBetProfitDao implements Dao<LeagueBetProfit> {
    INSTANCE;

    private ResultSetHandler<LeagueBetProfit> resultSetHandler;

    LeagueBetProfitDao() {
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("id", "id");
        columnMap.put("site_id", "siteId");
        columnMap.put("leagueID", "leagueId");
        columnMap.put("sideID", "sideId");
        columnMap.put("profit", "profit");
        columnMap.put("created_at", "createdAt");
        columnMap.put("updated_at", "updatedAt");
        BeanProcessor bs = new BeanProcessor(columnMap);
        resultSetHandler = new BeanHandler<>(LeagueBetProfit.class, new BasicRowProcessor(bs));
    }

    @Override
    public LeagueBetProfit findById(Connection connection, Long id) throws SQLException {
        String sql = "select * from leaguebet_profit where id = ?";
        return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, id);
    }

    public LeagueBetProfit findByLeagueIdAndSideIdAndSiteId(Connection connection, Long leagueId, Long sideId, Long siteId) throws SQLException {
        String sql = "select * from leaguebet_profit where leagueID = ? and sideID = ? and site_id = ? ";
        return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, leagueId, sideId, siteId);
    }

    @Override
    public Integer updateById(Connection connection, LeagueBetProfit leagueBetProfit) throws SQLException {
        return null;
    }

    @Override
    public Integer insert(Connection connection, LeagueBetProfit leagueBetProfit) throws SQLException {
        return null;
    }
}
