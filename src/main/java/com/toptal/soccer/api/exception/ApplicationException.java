package com.toptal.soccer.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown if some programming or system error occurs during the execution
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ApplicationException extends RuntimeException{
    public ApplicationException(String s) {
        super(s);
    }
}
