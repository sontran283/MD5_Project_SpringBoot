package com.ra.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String userIdNotFound) {
        super(userIdNotFound);
    }
}
