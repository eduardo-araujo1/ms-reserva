package com.eduardo.msreservation.dto;

import com.eduardo.msreservation.enums.EStatus;

import java.time.LocalDate;
import java.util.UUID;

public record ReservationResponseDto(
        UUID reservationId,
        String propertyId,
        String userId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Double totalAmount,
        EStatus status
        ) {
}
