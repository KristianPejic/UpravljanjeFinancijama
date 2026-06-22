package org.example.sustavzaupravljajeosobnimfinancijama.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
