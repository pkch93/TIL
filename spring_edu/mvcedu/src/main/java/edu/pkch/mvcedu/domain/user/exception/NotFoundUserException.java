package edu.pkch.mvcedu.domain.user.exception;

import edu.pkch.mvcedu.domain.exception.NotFoundResourceException;

public class NotFoundUserException extends NotFoundResourceException {
    private static final String ERROR_MESSAGE = "해당 ID의 유저가 존재하지 않습니다.";

    public NotFoundUserException() {
        super(ERROR_MESSAGE);
    }
}
