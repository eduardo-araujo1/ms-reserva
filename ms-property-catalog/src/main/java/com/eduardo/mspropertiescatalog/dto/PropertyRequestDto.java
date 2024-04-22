package com.eduardo.mspropertiescatalog.dto;

import com.eduardo.mspropertiescatalog.enums.ECity;

public record PropertyRequestDto(
        String title,
        String address,
        String description,
        ECity city,
        Double pricePerNight,
        String imageUrl
) {
}
