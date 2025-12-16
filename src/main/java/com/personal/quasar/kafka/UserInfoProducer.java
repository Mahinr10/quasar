package com.personal.quasar.kafka;

import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserInfoProducer {

    private static final String TOPIC = "red-giant.user";

    private KafkaTemplate<String, UserDTO> kafkaTemplate;

    public void produce(UserDTO user) {
        String key = user.getId();
        kafkaTemplate.send(TOPIC, user)
                .whenComplete((result, ex) -> {
                    if(ex != null) {
                        ex.printStackTrace();
                    } else {
                        var meta = result.getRecordMetadata();
                        log.info("Sent to topic={} partition={} offset={}",
                                meta.topic(), meta.partition(), meta.offset());
                    }
                });
    }
}
