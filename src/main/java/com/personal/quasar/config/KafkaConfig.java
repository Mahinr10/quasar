package com.personal.quasar.config;

import com.personal.quasar.config.kafka.KafkaTopic;
//import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.support.serializer.JsonSerializer;


@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic userTopic(KafkaTopic kafkaTopic) {
        return TopicBuilder.name(kafkaTopic.userTopic())
                .partitions(2)
                .replicas(3)
                .build();
    }

//    @Bean
//    public ProducerFactory<String, TaskDTO> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(
//                Map.of(
//                        "bootstrap.servers", "localhost:9092",
//                        "key.serializer", StringSerializer.class,
//                        "value.serializer", JsonSerializer.class
//                )
//        );
//    }
//
//    @Bean
//    public KafkaTemplate<String, TaskDTO> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
}
