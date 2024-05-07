package com.eduardo.mspropertiescatalog.dto;

import com.eduardo.mspropertiescatalog.enums.ECity;

import java.math.BigDecimal;
import java.util.UUID;

public record PropertyResponseDto(
        UUID propertyId,
        String title,
        String address,
        String description,
        ECity city,
        BigDecimal pricePerNight,
        String imageUrl
) {
}
