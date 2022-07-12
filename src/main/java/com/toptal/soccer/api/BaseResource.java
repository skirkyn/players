package com.toptal.soccer.api;

import com.toptal.soccer.api.exception.NotAuthorizedException;
import com.toptal.soccer.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
public abstract class BaseResource {

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException illegalStateException) {
        log.error(illegalStateException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(illegalStateException.getMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        log.error(illegalArgumentException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(illegalArgumentException.getMessage());
    }

    @ExceptionHandler(value = SecurityException.class)
    public ResponseEntity<?> handleSecurityException(SecurityException securityException) {
        log.error(securityException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(securityException.getMessage());
    }

    @ExceptionHandler(value = NotAuthorizedException.class)
    public ResponseEntity<?> handleNotAuthorized(NotAuthorizedException securityException) {
        log.error(securityException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(securityException.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleAppException(Exception exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }
}
