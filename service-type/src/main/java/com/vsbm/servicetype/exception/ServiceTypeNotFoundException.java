package com.vsbm.servicetype.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ServiceTypeNotFoundException extends RuntimeException {
    public ServiceTypeNotFoundException(String message) {
        super(message);
    }
}
