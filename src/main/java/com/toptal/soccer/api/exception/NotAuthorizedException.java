package com.toptal.soccer.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown if the uswer is not logged in or if the user is trying ti access a resource they are not authorized to
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String s) {
        super(s);
    }
}
