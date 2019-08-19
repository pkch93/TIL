package edu.pkch.mvcedu.domain.exception;

public class NotFoundResourceException extends RuntimeException {
    public NotFoundResourceException(final String message) {
        super(message);
    }
}
