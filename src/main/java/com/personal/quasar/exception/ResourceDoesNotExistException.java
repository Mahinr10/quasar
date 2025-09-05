package com.personal.quasar.exception;

public class ResourceDoesNotExistException extends Exception {
    public static final String MESSAGE = "Resource of type %s with id %s does not exist";
    public ResourceDoesNotExistException(String resourceType, String resourceId) {
        super(String.format(MESSAGE, resourceType, resourceId));
    }

    public ResourceDoesNotExistException(String message) {
        super(message);
    }

}
