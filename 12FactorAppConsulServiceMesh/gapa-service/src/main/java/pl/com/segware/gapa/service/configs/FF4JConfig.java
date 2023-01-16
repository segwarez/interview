package pl.com.segware.gapa.service.configs;

import org.ff4j.FF4j;
import org.ff4j.springjdbc.store.EventRepositorySpringJdbc;
import org.ff4j.springjdbc.store.FeatureStoreSpringJdbc;
import org.ff4j.springjdbc.store.PropertyStoreSpringJdbc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class FF4JConfig {
    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String jdbcUserName;
    @Value("${spring.datasource.password}")
    private String jdbcPassword;
    @Value("${spring.datasource.driver-class-name}")
    private String jdbcDriver;

    @Bean
    DataSource dataSource() {
        DriverManagerDataSource jdbc = new DriverManagerDataSource();
        jdbc.setDriverClassName(jdbcDriver);
        jdbc.setUrl(jdbcUrl);
        jdbc.setPassword(jdbcPassword);
        jdbc.setUsername(jdbcUserName);
        return jdbc;
    }

    @Bean
    FF4j getFF4j(DataSource dataSource) {
        FF4j ff4j = new FF4j();
        ff4j.setFeatureStore(new FeatureStoreSpringJdbc(dataSource));
        ff4j.setPropertiesStore(new PropertyStoreSpringJdbc(dataSource));
        ff4j.setEventRepository(new EventRepositorySpringJdbc(dataSource));
        ff4j.audit(true);
        return ff4j;
    }
}
