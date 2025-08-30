package com.personal.quasar.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "mongodb-config")
@Data
@NoArgsConstructor
public class MongoConfig {
    private String replicaSet;
    private List<String> hosts;
    private String database;
    @Bean
    public MongoClient mongoClient() {
        String connectionString = String.format("mongodb://%s/%s?replicaSet=%s",
                String.join(",", hosts), database, replicaSet);
        return MongoClients.create(connectionString);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), database);
    }
}