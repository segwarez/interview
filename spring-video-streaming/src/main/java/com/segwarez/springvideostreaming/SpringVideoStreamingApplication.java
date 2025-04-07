package com.segwarez.springvideostreaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
public class SpringVideoStreamingApplication {
    public static void main(String[] args) {
        Locale.setDefault(Locale.UK);
        SpringApplication.run(SpringVideoStreamingApplication.class, args);
    }
}
