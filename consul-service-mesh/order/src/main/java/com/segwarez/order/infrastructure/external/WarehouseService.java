package com.segwarez.order.infrastructure.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class WarehouseService {
    @Value("${warehouse.host}")
    private String host;
    @Value("${warehouse.port}")
    private int port;

    private final RestClient restClient;

    public boolean reserve() {
        var url = String.format("http://%s:%d/reserve", host, port);
        return Boolean.TRUE.equals(restClient.get()
                .uri(url)
                .exchange((req, res) ->
                        res.getStatusCode().is2xxSuccessful()));
    }
}
