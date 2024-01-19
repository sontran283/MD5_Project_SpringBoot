package com.ra.exception;

public class UpdateFailException extends IllegalArgumentException {
    public UpdateFailException(String message) {
        super(message);
    }
}
