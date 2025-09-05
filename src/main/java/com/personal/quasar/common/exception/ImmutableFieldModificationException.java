package com.personal.quasar.common.exception;

import java.util.List;

public class ImmutableFieldModificationException extends Exception {

    public static final String MESSAGE = "Attempted to modify an immutable fields - {%s}";
    public ImmutableFieldModificationException(String message) {
        super(message);
    }

    public ImmutableFieldModificationException(List<String> fieldNames) {
        super(String.format(MESSAGE, String.join(",", fieldNames)));
    }
}
