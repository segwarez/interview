package com.segwarez.rcedemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@RestController
@RequestMapping("/vulnerable")
public class VulnerableController {

    @Autowired
    private ConfigurableBeanFactory beanFactory;

    @PostMapping("/deserialize")
    public ResponseEntity<String> deserialize(@RequestBody String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        Object result = mapper.readValue(json, Object.class);
        return ResponseEntity.ok(result.getClass().getName());
    }

    @PostMapping("/expression")
    public ResponseEntity<Object> expression(@RequestBody String expression) {
        SpelExpressionParser parser = new SpelExpressionParser();
        Object result = parser.parseExpression(expression)
                .getValue(new BeanExpressionContext(beanFactory, null));
        return ResponseEntity.ok(result.getClass().getName());
    }

    @PostMapping("/yaml")
    public ResponseEntity<Object> yaml(@RequestBody String yaml) {
        Yaml parser = new Yaml();
        Object result = parser.loadAs(yaml, Object.class);
        return ResponseEntity.ok(result.getClass().getName());
    }

    @PostMapping(value = "/ois")
    public ResponseEntity<String> ois(@RequestBody byte[] payload) throws IOException, ClassNotFoundException {
        ByteArrayInputStream dataInputStream = new ByteArrayInputStream(payload);
        ObjectInputStream objectInputStream = new ObjectInputStream(dataInputStream);
        Object result = objectInputStream.readObject();
        return ResponseEntity.ok(result.getClass().getName());
    }
}
