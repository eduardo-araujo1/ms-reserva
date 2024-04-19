package com.eduardo.msuser.exception;

import org.springframework.http.HttpStatus;

public record ApiErrorMessage(
        HttpStatus status,
        String message
) {
}
