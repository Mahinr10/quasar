package com.personal.quasar.common;

import com.personal.quasar.kafka.UserInfoProducer;
import com.personal.quasar.model.dto.TaskDTO;
import com.personal.quasar.model.dto.TimeZoneDTO;
import com.personal.quasar.model.dto.UserDTO;
import com.personal.quasar.model.entity.Task;
import com.personal.quasar.service.TimeZoneService;
import com.personal.quasar.service.impl.TaskService;
import com.personal.quasar.service.impl.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.Math.abs;

@Component
@Slf4j
@AllArgsConstructor
public class NightlyTaskScheduler {

    UserInfoProducer userInfoProducer;

    @Scheduled(cron = "0 * * * * *")
    public void produceMessage() {
        log.info("producer starter");
        var user = new UserDTO();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("test-email");
        user.setFirstName("test-name");
        //userInfoProducer.produce(user);
    }
}
