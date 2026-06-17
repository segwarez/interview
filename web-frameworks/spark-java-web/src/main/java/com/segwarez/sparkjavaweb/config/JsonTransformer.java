package com.segwarez.sparkjavaweb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import spark.ResponseTransformer;

@RequiredArgsConstructor
public class JsonTransformer implements ResponseTransformer {
    private final ObjectMapper objectMapper;

    @Override
    public String render(Object model) throws Exception {
        if (model == null) {
            return "";
        }
        return objectMapper.writeValueAsString(model);
    }
}