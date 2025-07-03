package com.projects.airBnbApp.exception;

//This is a custom Exception

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
