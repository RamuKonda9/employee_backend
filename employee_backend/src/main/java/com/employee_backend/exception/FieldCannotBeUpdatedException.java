package com.employee_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// This is still a 400 Bad Request because the client
// is trying to do something that is not allowed.
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FieldCannotBeUpdatedException extends RuntimeException {
    public FieldCannotBeUpdatedException(String message) {
        super(message);
    }
}