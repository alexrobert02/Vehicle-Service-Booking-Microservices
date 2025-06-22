package com.appointment.exception;

public class AppointmentNotFound extends RuntimeException {
    public AppointmentNotFound(String message) {
        super(message);
    }
}
