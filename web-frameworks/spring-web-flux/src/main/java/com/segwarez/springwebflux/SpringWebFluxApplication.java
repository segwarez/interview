package com.segwarez.springwebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.util.Locale;

@EnableWebFlux
@SpringBootApplication
public class SpringWebFluxApplication {
    public static void main(String[] args) {
        Locale.setDefault(Locale.UK);
        SpringApplication.run(SpringWebFluxApplication.class, args);
    }
}
