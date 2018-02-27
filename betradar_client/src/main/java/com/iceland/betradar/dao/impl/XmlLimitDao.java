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

public enum XmlLimitDao implements Dao<XmlLimit>{
    INSTANCE;

    private ResultSetHandler<XmlLimit> resultSetHandler;

    XmlLimitDao() {
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("id", "id");
        columnMap.put("site_id", "siteId");
        columnMap.put("type", "type");
        columnMap.put("type_id", "typeId");
        columnMap.put("min_event", "minEvent");
        columnMap.put("won_limit", "wonLimit");
        columnMap.put("customer_limit", "customerLimit");
        columnMap.put("maxlimit", "maxLimit");
        columnMap.put("profit", "profit");
        columnMap.put("feed", "feed");
        columnMap.put("created_at", "createdAt");
        columnMap.put("updated_at", "updatedAt");
        BeanProcessor bs = new BeanProcessor(columnMap);
        resultSetHandler = new BeanHandler<>(XmlLimit.class, new BasicRowProcessor(bs));
    }

    @Override
    public XmlLimit findById(Connection connection, Long id) throws SQLException {
        String sql = "select * from xml_limit where id = ?";
        return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, id);
    }

    public XmlLimit findByTypeAndTypeId(Connection connection, String type, Long typeId) throws SQLException {
        String sql = "select * from xml_limit where type = ? and type_id = ? ";
        return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, type, typeId);
    }

    public XmlLimit findByTypeAndTypeIdAndSiteId(Connection connection, String type, Long typeId, Long siteId) throws SQLException {
        String sql = "select * from xml_limit where type = ? and type_id = ? and  site_id = ?";
        return IcelandDataSource.INSTANCE.run().query(connection, sql, resultSetHandler, type, typeId, siteId);
    }

    @Override
    public Integer updateById(Connection connection, XmlLimit xmlLimit) throws SQLException {
        return null;
    }

    @Override
    public Integer insert(Connection connection, XmlLimit xmlLimit) throws SQLException {
        return null;
    }
}
