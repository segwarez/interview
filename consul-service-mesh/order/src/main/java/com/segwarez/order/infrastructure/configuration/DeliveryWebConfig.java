package com.segwarez.order.infrastructure.configuration;

import com.segwarez.order.infrastructure.external.DeliveryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class DeliveryWebConfig {
    @Value("${delivery.host}")
    private String host;
    @Value("${delivery.port}")
    private int port;

    @Bean
    RestClient deliveryRestClient() {
        return RestClient.builder()
                .baseUrl("http://" + host + ":" + port)
                .build();
    }

    @Bean
    DeliveryClient deliveryClient(RestClient deliveryRestClient) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(deliveryRestClient))
                .build()
                .createClient(DeliveryClient.class);
    }
}