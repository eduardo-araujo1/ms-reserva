package com.eduardo.msreservation.client.dto;

import java.math.BigDecimal;

public record PropertyInfoDto(
        String propertyId,
        BigDecimal pricePerNight
) {
}
