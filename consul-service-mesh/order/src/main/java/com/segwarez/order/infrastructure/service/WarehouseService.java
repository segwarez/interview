package com.segwarez.order.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    @Value("${inventory.host:localhost}")
    private String host;
    @Value("${inventory.port:8082}")
    private int port;

    private final RestClient restClient;

    public boolean reserve() {
        var url = String.format("http://%s:%d/reserve", host, port);
        return restClient.get()
                .uri(url)
                .exchange((req, res) ->
                        res.getStatusCode().is2xxSuccessful());
    }
}
