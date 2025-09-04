package com.personal.quasar.service.validator;

import com.personal.quasar.exception.InvalidFieldException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TaskValidatorImpl implements TaskValidator {
    @Override
    public void validateScheduledDate(Date date) throws InvalidFieldException {
        if (date.before(new Date())) {
            throw new InvalidFieldException("Scheduled date must be in the future");
        }
    }
}
