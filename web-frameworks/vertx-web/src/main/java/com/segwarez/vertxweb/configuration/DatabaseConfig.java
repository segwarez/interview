package com.segwarez.vertxweb.configuration;

import io.vertx.core.Vertx;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;

public class DatabaseConfig {
    private static final String HOST_CONFIG = "datasource.host";
    private static final String PORT_CONFIG = "datasource.port";
    private static final String DATABASE_CONFIG = "datasource.database";
    private static final String USERNAME_CONFIG = "datasource.username";
    private static final String PASSWORD_CONFIG = "datasource.password";

    private DatabaseConfig() {
    }

    public static Pool buildDbClient(Vertx vertx) {
        var properties = ApplicationConfig.getInstance().getProperties();
        var connectOptions = new PgConnectOptions()
            .setPort(Integer.parseInt(properties.getProperty(PORT_CONFIG)))
            .setHost(properties.getProperty(HOST_CONFIG))
            .setDatabase(properties.getProperty(DATABASE_CONFIG))
            .setUser(properties.getProperty(USERNAME_CONFIG))
            .setPassword(properties.getProperty(PASSWORD_CONFIG));
        return PgBuilder
            .pool()
            .with(new PoolOptions().setMaxSize(5))
            .connectingTo(connectOptions)
            .using(vertx)
            .build();
    }
}
