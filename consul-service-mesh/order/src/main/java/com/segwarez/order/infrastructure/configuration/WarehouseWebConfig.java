package com.segwarez.order.infrastructure.configuration;

import com.segwarez.order.infrastructure.external.WarehouseClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.io.IOException;
import java.net.URI;

@Configuration
public class WarehouseWebConfig {
    @Value("${warehouse.host}")
    private String host;
    @Value("${warehouse.port}")
    private int port;

    @Bean
    RestClient warehouseRestClient() {
        return RestClient.builder()
                .baseUrl("http://" + host + ":" + port)
                .defaultStatusHandler(new ErrorHandler())
                .build();
    }

    @Bean
    WarehouseClient warehouseClient(RestClient warehouseRestClient) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(warehouseRestClient))
                .build()
                .createClient(WarehouseClient.class);
    }

    class ErrorHandler extends DefaultResponseErrorHandler {
        @Override
        public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
            if (response.getStatusCode().value() == 409) return;
            this.handleError(response, response.getStatusCode(), url, method);
        }
    }
}