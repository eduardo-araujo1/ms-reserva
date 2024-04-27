package com.eduardo.msreservation.exception;

public class ReservationDateUnavailableException extends RuntimeException {
    public ReservationDateUnavailableException(String message) {
        super(message);
    }
}