package com.segwarez.sparkjavaweb.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;

import static org.jooq.impl.DSL.using;

public final class DatabaseConfig {
    private DatabaseConfig() {
    }

    public static HikariDataSource dataSource(ApplicationConfig config) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(config.database().getUrl());
        hikariConfig.setUsername(config.database().getUsername());
        hikariConfig.setPassword(config.database().getPassword());

        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(2);

        return new HikariDataSource(hikariConfig);
    }

    public static DSLContext dslContext(HikariDataSource dataSource) {
        return using(dataSource, SQLDialect.POSTGRES);
    }
}