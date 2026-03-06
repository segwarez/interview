package com.segwarez.ping.infrastructure.configuration;

import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;

@Configuration
public class WebConfig {
    @Bean
    RestClient restClient(SslBundles sslBundles) {
        var bundle = sslBundles.getBundle("ping-bundle");
        var httpClient = HttpClient.newBuilder()
                .sslContext(bundle.createSslContext())
                .build();
        return RestClient.builder()
                .requestFactory(new JdkClientHttpRequestFactory(httpClient))
                .build();
    }
}
