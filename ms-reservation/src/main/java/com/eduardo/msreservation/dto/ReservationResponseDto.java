package com.eduardo.msreservation.dto;

import com.eduardo.msreservation.enums.EStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ReservationResponseDto(
        UUID reservationId,
        String propertyId,
        String userId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        BigDecimal totalAmount,
        EStatus status,
        String userEmail,
        String username
        ) {
}
