package com.dawidrozewski.sandbox.security.exception;

import com.dawidrozewski.sandbox.common.exception.BusinessException;

public class RegisterException extends BusinessException {
    public RegisterException(String message) {
        super(message);
    }
}
