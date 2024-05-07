package com.eduardo.mspropertiescatalog.dto;

import com.eduardo.mspropertiescatalog.enums.ECity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PropertyRequestDto(
        @NotBlank(message = "O titulo não pode estar em branco.")
        String title,
        @NotBlank(message = "O endereço não pode estar em branco.")
        String address,
        @NotBlank(message = "A descrição não pode estar em branco.")
        String description,
        @NotNull(message = "A cidade não pode estar em branco.")
        ECity city,
        @NotNull(message = "O preço por noite não pode estar em branco.")
        @Min(value = 1, message = "O preço por noite não pode ser menor que R$1.00.")
        BigDecimal pricePerNight,
        String imageUrl
) {
}
