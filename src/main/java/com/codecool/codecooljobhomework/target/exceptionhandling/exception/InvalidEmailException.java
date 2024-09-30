package com.codecool.codecooljobhomework.target.exceptionhandling.exception;

public class InvalidEmailException extends SynchronizationException {
    public InvalidEmailException(String message) {
        super(message);
    }
}
