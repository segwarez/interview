package com.segwarez.ping.infrastructure.configuration;

import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class WebConfig {
    @Bean
    RestClient restClient(RestClient.Builder builder, RestClientSsl ssl) {
        return builder
                .apply(ssl.fromBundle("ping-bundle"))
                .build();
    }
}
