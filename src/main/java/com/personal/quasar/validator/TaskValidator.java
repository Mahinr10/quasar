package com.personal.quasar.validator;

import com.personal.quasar.common.exception.InvalidFieldException;

import java.util.Date;

public interface TaskValidator {
    void validateScheduledDate(Date date) throws InvalidFieldException;
}
