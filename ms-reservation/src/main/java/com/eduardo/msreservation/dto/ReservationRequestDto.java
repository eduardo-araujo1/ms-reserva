package com.eduardo.msreservation.dto;

import java.time.LocalDate;

public record ReservationRequestDto(
        String propertyId,
        String userId,
        LocalDate checkinDate,
        LocalDate checkOutDate
) {
}
