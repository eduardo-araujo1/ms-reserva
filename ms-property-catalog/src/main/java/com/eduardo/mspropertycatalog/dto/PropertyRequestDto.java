package com.eduardo.mspropertycatalog.dto;

import com.eduardo.mspropertycatalog.enums.ECity;

public record PropertyRequestDto(
        String title,
        String address,
        String description,
        ECity city,
        Double pricePerNight,
        String imageUrl
) {
}
