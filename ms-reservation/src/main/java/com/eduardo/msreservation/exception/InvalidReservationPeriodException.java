package com.eduardo.msreservation.exception;

public class InvalidReservationPeriodException extends RuntimeException {
    public InvalidReservationPeriodException(String message) {
        super(message);
    }
}
