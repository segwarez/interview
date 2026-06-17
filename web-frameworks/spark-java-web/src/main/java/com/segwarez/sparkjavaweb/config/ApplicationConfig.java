package com.segwarez.sparkjavaweb.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.Value;

public final class ApplicationConfig {
    private static final Config CONFIG = ConfigFactory.load();
    private final Server server;
    private final Database database;

    private ApplicationConfig() {
        this.server = new Server(CONFIG.getConfig("server"));
        this.database = new Database(CONFIG.getConfig("database"));
    }

    public static ApplicationConfig load() {
        return new ApplicationConfig();
    }

    public Server server() {
        return server;
    }

    public Database database() {
        return database;
    }

    @Value
    public static class Server {
        int port;

        public Server(Config config) {
            this.port = config.getInt("port");
        }
    }

    @Value
    public static class Database {
        String url;
        String username;
        String password;

        public Database(Config config) {
            this.url = config.getString("url");
            this.username = config.getString("username");
            this.password = config.getString("password");
        }
    }
}