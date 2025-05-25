package com.segwarez.rcedemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@RestController
public class VulnerableController {

    @PostMapping(value = "/vulnerable")
    public ResponseEntity<String> exploit(@RequestBody byte[] payload) throws IOException, ClassNotFoundException {
        var dataInputStream = new ByteArrayInputStream(payload);
        var objectInputStream = new ObjectInputStream(dataInputStream);
        var object = objectInputStream.readObject();
        return ResponseEntity.ok(object.getClass().getName());
    }
}
