package com.segwarez.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WorkerApplication {
	static void main(String[] args) {
		SpringApplication.run(WorkerApplication.class, args);
	}
}