package com.priyanshparekh.ping.error.exception;

public class ChatExistsException extends RuntimeException {

    public ChatExistsException() {
        super("Chat Already Exists");
    }

}
