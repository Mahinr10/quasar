package com.personal.quasar.common;

import com.personal.quasar.service.impl.TaskService;
import com.personal.quasar.service.impl.UserService;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.Math.abs;

@Component
public class NightlyTaskScheduler {
    @Autowired
    TaskService taskService;
    @Scheduled(cron = "0 0,15,30,45 * * * ?")
    public void moveTaskToTheNextDay() {
        System.out.println("Scheduled task executed at " + System.currentTimeMillis());
        getTimeZone().forEach(System.out::println);
    }

    private List<String> getTimeZone() {
        List<String> zones = new ArrayList<>();
        for(String zoneId : ZoneId.getAvailableZoneIds()) {
            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(zoneId));
            LocalTime localTime = LocalTime.of(0, 0);
            LocalTime localTime1 = zonedDateTime.toLocalTime();
            if(abs(Duration.between(localTime1, localTime).toMinutes()) <= 10) {
                zones.add(zoneId);
            }
        }
        return zones;
    }
}
