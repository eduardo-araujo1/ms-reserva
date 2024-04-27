package com.eduardo.msreservation.exception;

public class ReservationNotFoundException extends RuntimeException{
    public ReservationNotFoundException(String message){
        super(message);
    }
}
