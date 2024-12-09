package com.segwarez.pong.application;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PongController {
    @GetMapping(value = "/pong", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> pong(){
        return ResponseEntity.ok("Pong!");
    }
}
