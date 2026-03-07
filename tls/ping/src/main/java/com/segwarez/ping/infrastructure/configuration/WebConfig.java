package com.segwarez.ping.infrastructure.configuration;

import com.segwarez.ping.infrastructure.external.PongClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.net.http.HttpClient;

@Configuration
public class WebConfig {
    @Value("${pong.host:localhost}")
    private String host;
    @Value("${pong.port:8443}")
    private int port;

    @Bean
    RestClient restClient(SslBundles sslBundles) {
        var bundle = sslBundles.getBundle("ping-bundle");
        var httpClient = HttpClient.newBuilder()
                .sslContext(bundle.createSslContext())
                .build();

        return RestClient.builder()
                .baseUrl("https://" + host + ":" + port)
                .requestFactory(new JdkClientHttpRequestFactory(httpClient))
                .build();
    }

    @Bean
    PongClient pongClient(RestClient restClient) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(PongClient.class);
    }
}