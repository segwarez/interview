package com.segwarez.order.infrastructure.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class DeliveryService {
    @Value("${delivery.host:localhost}")
    private String host;
    @Value("${delivery.port:8084}")
    private int port;

    private final RestClient restClient;

    public String deliver() {
        var url = String.format("http://%s:%d/deliver", host, port);
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }
}
