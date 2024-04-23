package com.eduardo.mspropertiescatalog.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(AddressAlreadyRegisteredException.class)
    public ResponseEntity<ApiErrorMessage> addressException(AddressAlreadyRegisteredException exception){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorMessage( HttpStatus.CONFLICT, exception.getMessage()));
    }

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<ApiErrorMessage> propertyNotFound(PropertyNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorMessage> methodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  BindingResult result){
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY,"Campo(s) inválidos", result));
    }

}
