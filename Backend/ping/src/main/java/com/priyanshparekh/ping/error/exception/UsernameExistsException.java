package com.priyanshparekh.ping.error.exception;

public class UsernameExistsException extends RuntimeException {

    public UsernameExistsException() {
        super("User already exists!");
    }

}
