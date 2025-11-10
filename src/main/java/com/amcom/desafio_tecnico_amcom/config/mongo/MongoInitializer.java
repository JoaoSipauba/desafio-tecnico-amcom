package com.amcom.desafio_tecnico_amcom.config.mongo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MongoInitializer implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;

    public MongoInitializer(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) {
        createDatabaseIfNotExists();
        createCollectionIfNotExists();
    }

    private void createDatabaseIfNotExists() {
        try {
            String dbName = mongoTemplate.getDb().getName();
            mongoTemplate.getDb().listCollectionNames();
            log.debug("Database verified or created successfully: {}", dbName);
        } catch (Exception e) {
            log.debug("Error verifying or creating database: {}", e.getMessage());
        }
    }

    private void createCollectionIfNotExists() {
        if (!mongoTemplate.collectionExists("orders")) {
            mongoTemplate.createCollection("orders");
            log.debug("Collection created successfully: orders");
        } else {
            log.debug("Collection already exists: orders");
        }
    }
}