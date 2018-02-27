package com.iceland.betradar.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface Dao<T> {

	T findById(Connection connection, Long id) throws SQLException;

	Integer updateById(Connection connection, T t) throws SQLException;

	Integer insert(Connection connection, T t) throws SQLException;
}
