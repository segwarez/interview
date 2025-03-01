package com.segwarez.ping.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class PongService {
    private final RestClient restClient;
    @Value("${pong.host:localhost}")
    private String host;
    @Value("${pong.port:8443}")
    private int port;

    public String pong() {
        var url = String.format("https://%s:%d/pong", host, port);
        var response = restClient.get().uri(url).retrieve();
        return response.body(String.class);
    }
}
