package com.eduardo.msreservation.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class ApiErrorMessage{

    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String,String> errors;


    public ApiErrorMessage(HttpStatus status, String message, BindingResult result){
        this.status = status.value();
        this.message = message;
        addErrors(result);
    }

    public ApiErrorMessage(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }

    private void addErrors(BindingResult result) {
        this.errors = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()){
            this.errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }

}

