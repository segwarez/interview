package com.segwarez.nope.application.rest;

import com.segwarez.nope.infrastructure.PongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NopeController {
    private PongService pongService;

    @GetMapping(value = "/nope", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> nope(){
        return ResponseEntity.ok(pongService.pong());
    }
}
