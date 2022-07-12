package com.toptal.soccer.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when the client send an invalid request
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(Exception e) {
        super(e);
    }
    public InvalidRequestException(String m) {
        super(m);
    }
}
