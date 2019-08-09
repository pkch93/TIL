package edu.pkch.mvcedu.api.user.exception;

import edu.pkch.mvcedu.api.exception.NotExistResourceException;

public class NotExistUserException extends NotExistResourceException {
    private static final String ERROR_MESSAGE = "해당 ID의 유저가 존재하지 않습니다.";

    public NotExistUserException() {
        super(ERROR_MESSAGE);
    }
}
