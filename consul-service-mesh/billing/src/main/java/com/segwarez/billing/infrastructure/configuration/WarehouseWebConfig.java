package com.segwarez.billing.infrastructure.configuration;

import com.segwarez.billing.infrastructure.external.WarehouseClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WarehouseWebConfig {
    @Value("${warehouse.url}")
    private String warehouseUrl;

    @Bean
    RestClient warehouseRestClient() {
        return RestClient.builder()
                .baseUrl(warehouseUrl)
                .build();
    }

    @Bean
    WarehouseClient warehouseClient(RestClient warehouseRestClient) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(warehouseRestClient))
                .build()
                .createClient(WarehouseClient.class);
    }
}