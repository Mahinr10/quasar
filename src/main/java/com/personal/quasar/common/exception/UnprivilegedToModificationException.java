package com.personal.quasar.common.exception;

import com.personal.quasar.service.UserProfileFacade;

import java.util.List;

public class UnprivilegedToModificationException extends Exception {
    public UnprivilegedToModificationException(String message) {
        super(message);
    }
}
