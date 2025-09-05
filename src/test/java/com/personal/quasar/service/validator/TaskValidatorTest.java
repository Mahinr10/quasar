package com.personal.quasar.service.validator;

import com.personal.quasar.UnitTest;
import com.personal.quasar.common.exception.InvalidFieldException;
import com.personal.quasar.validator.TaskValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.Date;

public class TaskValidatorTest extends UnitTest {

    @Autowired
    private TaskValidator taskValidator;

    @Test
    public void validateScheduledDateTest() {
        Date date = new Date();
        try {
            taskValidator.validateScheduledDate(date);
        } catch (InvalidFieldException e) {
            Assertions.fail("Validation should pass for current date");
        }
    }

    @Test
    public void validateScheduledDateForEarlierDateTest() throws Exception {
        Date date = Date.from(ZonedDateTime.now().minusDays(1).toInstant());
        var result = Assertions.assertThrows(InvalidFieldException.class, () -> {
            taskValidator.validateScheduledDate(date);
        });
        Assertions.assertEquals("Scheduled date must be in the future", result.getMessage());
    }

}
