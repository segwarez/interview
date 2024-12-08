package com.segwarez.vertxweb.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {
    private static ApplicationConfig instance;
    private final Properties properties;

    private ApplicationConfig() {
        this.properties = readProperties();
    }

    public static ApplicationConfig getInstance() {
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }

    public Properties getProperties() {
        return properties;
    }

    private Properties readProperties() {
        var props = new Properties();
        try (InputStream is = getClass()
            .getClassLoader()
            .getResourceAsStream("application.properties")
        ) {
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
}
