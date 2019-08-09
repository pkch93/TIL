package edu.pkch.mvcedu.api.exception;

public class NotExistResourceException extends RuntimeException {
    public NotExistResourceException(final String message) {
        super(message);
    }
}
