package com.eduardo.mspropertiescatalog.exception;

public class AddressAlreadyRegisteredException extends RuntimeException{
    public AddressAlreadyRegisteredException(String message){
        super(message);
    }
}
