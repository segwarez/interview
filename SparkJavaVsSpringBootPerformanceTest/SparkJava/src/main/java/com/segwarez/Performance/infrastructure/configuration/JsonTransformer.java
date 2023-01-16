package com.segwarez.Performance.infrastructure.configuration;

import com.google.gson.*;
import spark.ResponseTransformer;

import java.lang.reflect.Type;
import java.time.Instant;

public class JsonTransformer implements ResponseTransformer {

    private Gson gson;

    public JsonTransformer() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Instant.class, new InstantDeserializer());
        gson = gsonBuilder.create();
    }

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

    class InstantDeserializer implements JsonSerializer<Instant> {
        @Override
        public JsonElement serialize(Instant instant, Type type,
                                     JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(instant.toString());
        }
    }
}
