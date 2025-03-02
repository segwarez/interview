package com.segwarez.micronautweb;

import io.micronaut.runtime.Micronaut;
import io.micronaut.serde.annotation.SerdeImport;

@SerdeImport(packageName = "com.segwarez.micronautweb.domain")
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}