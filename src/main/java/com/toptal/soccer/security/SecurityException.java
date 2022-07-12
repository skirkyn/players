package com.toptal.soccer.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception that represents all the security issues
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class SecurityException extends RuntimeException{
    public SecurityException(final String message){
        super(message);
    }
}
