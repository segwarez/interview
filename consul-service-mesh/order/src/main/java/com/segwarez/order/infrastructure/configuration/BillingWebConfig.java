package com.segwarez.order.infrastructure.configuration;

import com.segwarez.order.infrastructure.external.BillingClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class BillingWebConfig {
    @Value("${billing.host}")
    private String host;
    @Value("${billing.port}")
    private int port;

    @Bean
    RestClient billingRestClient() {
        return RestClient.builder()
                .baseUrl("http://" + host + ":" + port)
                .build();
    }

    @Bean
    BillingClient billingClient(RestClient billingRestClient) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(billingRestClient))
                .build()
                .createClient(BillingClient.class);
    }
}