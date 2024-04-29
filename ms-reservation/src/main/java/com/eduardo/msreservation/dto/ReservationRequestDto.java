package com.eduardo.msreservation.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationRequestDto(
        @NotBlank(message = "O campo propertyId é obrigatório.")
        String propertyId,
        @NotBlank(message = "O campo userId é obrigatório.")
        String userId,

        @NotNull(message = "Campo checkIn é obrigatório.")
        LocalDate checkInDate,

        @NotNull(message = "Campo checkOut é obrigatório.")
        LocalDate checkOutDate
) {
    public boolean isValidReservationPeriod() {
        return checkOutDate.isAfter(checkInDate) && !checkOutDate.isEqual(checkInDate);
    }
}
