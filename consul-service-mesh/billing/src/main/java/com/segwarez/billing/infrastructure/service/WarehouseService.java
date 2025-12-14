package com.segwarez.billing.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    @Value("${warehouse.host:localhost}")
    private String host;
    @Value("${warehouse.port:8082}")
    private int port;

    private final RestClient restClient;

    public void update() {
        var url = String.format("http://%s:%d/update", host, port);
        restClient.put()
                .uri(url)
                .retrieve()
                .toBodilessEntity();
    }
}
