package com.personal.quasar.service.validator;

import com.personal.quasar.exception.InvalidFieldException;

import java.util.Date;

public interface TaskValidator {
    void validateScheduledDate(Date date) throws InvalidFieldException;
}
