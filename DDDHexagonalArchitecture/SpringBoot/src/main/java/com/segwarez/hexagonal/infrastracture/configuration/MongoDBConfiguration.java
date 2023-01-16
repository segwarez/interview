package com.segwarez.hexagonal.infrastracture.configuration;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.segwarez.hexagonal.infrastracture.repository.mongo.SpringDataMongoOrderRepository;

@EnableMongoRepositories(basePackageClasses = SpringDataMongoOrderRepository.class)
public class MongoDBConfiguration {
}
