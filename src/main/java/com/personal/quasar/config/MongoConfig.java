package com.personal.quasar.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "mongodb-config")
public class MongoConfig {

    private String replicaSet;
    private List<String> hosts;
    private String database;

    public String getReplicaSet() { return replicaSet; }
    public void setReplicaSet(String replicaSet) { this.replicaSet = replicaSet; }
    public List<String> getHosts() { return hosts; }
    public void setHosts(List<String> hosts) { this.hosts = hosts; }
    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }

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