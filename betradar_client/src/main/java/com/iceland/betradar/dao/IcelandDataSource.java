package com.iceland.betradar.dao;

import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public enum IcelandDataSource {
	INSTANCE;
	private QueryRunner run;
	private HikariDataSource ds;

	IcelandDataSource() {
		ResourceBundle rb = ResourceBundle.getBundle("dbprofile");
		String environment = rb.getString("environment");


		HikariConfig config = new HikariConfig();

		switch (environment) {
			case "local" :
				config.setJdbcUrl("jdbc:mysql://localhost:3306/iceland_local?autoReconnect=true&useSSL=false");
				config.setUsername("${username.local}");
				config.setPassword("${password.local}");
				break;
			case "local_homestead":
				config.setJdbcUrl("jdbc:mysql://192.168.10.10:3306/iceland_test?autoReconnect=true&useSSL=false");
				config.setUsername("${username.local}");
				config.setPassword("${password.local}");
				break;
			case "production_test" :
				config.setJdbcUrl("jdbc:mysql://173.209.59.66/iceland_test?autoReconnect=true&useSSL=false");
				config.setUsername("${username.local}");
				config.setPassword("${password.local}");
				break;
			case "production" :
				config.setJdbcUrl("jdbc:mysql://173.209.59.66/iceland_db?autoReconnect=true&useSSL=false");
				config.setUsername("${username.local}");
				config.setPassword("${password.local}");
				break;
		}

		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.setAutoCommit(false);
		ds = new HikariDataSource(config);
		run = new QueryRunner(ds);
	}

	public QueryRunner run() {
		return run;
	}

	public com.zaxxer.hikari.HikariDataSource getDs() {
		return ds;
	}
}
