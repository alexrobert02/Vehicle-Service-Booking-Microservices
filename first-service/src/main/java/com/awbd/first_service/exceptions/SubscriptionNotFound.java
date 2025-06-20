package com.awbd.first_service.exceptions;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubscriptionNotFound extends RuntimeException {
    public SubscriptionNotFound(String message) {
        super(message);
    }
}
