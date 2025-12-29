package com.example.demo.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
