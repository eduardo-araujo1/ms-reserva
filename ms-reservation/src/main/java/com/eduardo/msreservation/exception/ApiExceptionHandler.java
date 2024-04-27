package com.eduardo.msreservation.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiErrorMessage> userException(UserException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(PropertyException.class)
    public ResponseEntity<ApiErrorMessage> propertyException(PropertyException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ApiErrorMessage> reservationNotFound(ReservationNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorMessage> methodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  BindingResult result) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) inválido(s)", result));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiErrorMessage> feignException(FeignException exception){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno."));
    }

    @ExceptionHandler(InvalidReservationPeriodException.class)
    public ResponseEntity<ApiErrorMessage> invalidReservation(InvalidReservationPeriodException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(ReservationDateUnavailableException.class)
    public ResponseEntity<ApiErrorMessage> reservationDateUnavaible(ReservationDateUnavailableException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }
}
