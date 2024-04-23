package com.eduardo.mspropertiescatalog.exception;

public class PropertyNotFoundException extends RuntimeException{
    public PropertyNotFoundException(String message){
        super(message);
    }
}
