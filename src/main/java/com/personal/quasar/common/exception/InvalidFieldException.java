package com.personal.quasar.common.exception;

import java.util.List;

public class InvalidFieldException extends Exception {
    public static final String MESSAGE = "The values provided for the following fields are incorrect - %s";
    public InvalidFieldException(String message) {
        super(message);
    }
    public InvalidFieldException(List<String> fields) {
        super(String.format(MESSAGE, String.join(", ", fields)));
    }
}
