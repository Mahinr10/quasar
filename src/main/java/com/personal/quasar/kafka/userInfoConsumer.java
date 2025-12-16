package com.personal.quasar.kafka;

import com.personal.quasar.model.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class userInfoConsumer {

    @KafkaListener(
            topics = "red-giant.user",
            groupId = "red-giant",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(@Payload UserDTO user,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        @Header(KafkaHeaders.OFFSET) int offset) {
        log.info("Consumed event: {}, partition: {}, offset: {}", user, partition, offset);
    }
}
