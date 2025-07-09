package com.vsbm.appointment.exception;

public class AppointmentNotFound extends RuntimeException {
    public AppointmentNotFound(String message) {
        super(message);
    }
}
