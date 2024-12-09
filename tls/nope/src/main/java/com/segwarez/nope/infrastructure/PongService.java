package com.segwarez.nope.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PongService {
    private RestTemplate restTemplate;
    @Value("${pong.hostname:127.0.0.1}")
    private String hostname;
    @Value("${pong.port:8443}")
    private int port;

    public String pong() {
        var url = String.format("https://%s:%d/pong", hostname, port);
        var response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
}
