package com.eduardo.mspropertiescatalog.dto;

import com.eduardo.mspropertiescatalog.enums.ECity;

import java.util.UUID;

public record PropertyResponseDto(
        UUID id,
        String title,
        String address,
        String description,
        ECity city,
        Double pricePerNight,
        String imageUrl
) {
}
