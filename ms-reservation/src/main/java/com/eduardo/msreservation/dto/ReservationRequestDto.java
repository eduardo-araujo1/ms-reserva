package com.eduardo.msreservation.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ReservationRequestDto(
        @NotBlank(message = "O campo propertyId é obrigatório.")
        String propertyId,
        @NotBlank(message = "O campo userId é obrigatório.")
        String userId,
        @FutureOrPresent(message = "A data de check-in deve ser no futuro ou no presente.")
        LocalDate checkInDate,
        @Future(message = "A data de check-out deve ser no futuro.")
        LocalDate checkOutDate
) {
    public boolean isValidReservationPeriod() {
        return checkOutDate.isAfter(checkInDate) && !checkOutDate.isEqual(checkInDate);
    }
}
