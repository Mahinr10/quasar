package com.personal.quasar.dao;

import com.personal.quasar.UnitTest;
import com.personal.quasar.model.entity.TimeZone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@ActiveProfiles("test")
public class TimeZoneRepositoryTest extends UnitTest {
    @Autowired
    TimeZoneRepository timeZoneRepository;

    @Test
    public void testFindIsIsDeletedFalse() {
        TimeZone timeZone1 = new TimeZone();
        timeZone1.setId(UUID.randomUUID().toString());
        timeZone1.setTimeZoneId("America/New_York");
        timeZone1.setIsDeleted(false);

        TimeZone timeZone2 = new TimeZone();
        timeZone2.setId(UUID.randomUUID().toString());
        timeZone2.setTimeZoneId("Europe/London");
        timeZone2.setIsDeleted(true);

        timeZoneRepository.save(timeZone1);
        timeZoneRepository.save(timeZone2);

        var result = timeZoneRepository.findByIsDeletedFalse();

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1, result.get().size());
        Assertions.assertEquals("America/New_York", result.get().get(0).getTimeZoneId());

        timeZoneRepository.deleteById(timeZone1.getId());
        timeZoneRepository.deleteById(timeZone2.getId());
    }

    @Test
    public void findByTimeZoneIdTest() {
        TimeZone timeZone = new TimeZone();
        timeZone.setId(UUID.randomUUID().toString());
        timeZone.setTimeZoneId("Asia/Tokyo");
        timeZone.setIsDeleted(false);

        timeZoneRepository.save(timeZone);

        var result = timeZoneRepository.findByTimeZoneId("Asia/Tokyo");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Asia/Tokyo", result.get().getTimeZoneId());

        timeZoneRepository.deleteById(timeZone.getId());
    }

    @Test
    public void findByTimeZoneIdIsDeletedFalseTest() {
        TimeZone timeZone1 = new TimeZone();
        timeZone1.setId(UUID.randomUUID().toString());
        timeZone1.setTimeZoneId("Asia/Tokyo");
        timeZone1.setIsDeleted(false);

        TimeZone timeZone2 = new TimeZone();
        timeZone2.setId(UUID.randomUUID().toString());
        timeZone2.setTimeZoneId("Europe/London");
        timeZone2.setIsDeleted(true);

        timeZoneRepository.save(timeZone1);
        timeZoneRepository.save(timeZone2);

        var result = timeZoneRepository.findByTimeZoneIdAndIsDeletedFalse(timeZone1.getTimeZoneId());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Asia/Tokyo", result.get().getTimeZoneId());

        result = timeZoneRepository.findByTimeZoneIdAndIsDeletedFalse(timeZone2.getTimeZoneId());

        Assertions.assertFalse(result.isPresent());

        timeZoneRepository.deleteById(timeZone1.getId());
        timeZoneRepository.deleteById(timeZone2.getId());
    }
}
