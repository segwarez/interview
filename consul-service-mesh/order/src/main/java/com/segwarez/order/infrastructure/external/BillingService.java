package com.segwarez.order.infrastructure.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class BillingService {
    @Value("${billing.host:localhost}")
    private String host;
    @Value("${billing.port:8083}")
    private int port;

    private final RestClient restClient;

    public void pay() {
        var url = String.format("http://%s:%d/pay", host, port);
        restClient.post()
                .uri(url)
                .retrieve()
                .toBodilessEntity();
    }
}
