package com.eduardo.msreservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PaymentDto(
        @NotNull(message = "O valor do pagamento é obrigatório.")
        Double amount,

        @NotBlank(message = "O número do cartão é obrigatório.")
        @Pattern(regexp = "\\d{16}", message = "O número do cartão deve ter 16 dígitos.")
        String cardNumber,

        @NotBlank(message = "O nome do titular do cartão é obrigatório.")
        String cardHolderName,

        @NotBlank(message = "A data de validade é obrigatória.")
        @Pattern(regexp = "\\d{2}/\\d{2}", message = "Formato de data de validade inválido. Use MM/AA.")
        String expirationDate,

        @NotBlank(message = "O código CVV é obrigatório.")
        @Pattern(regexp = "\\d{3}", message = "O código CVV deve ter 3 dígitos.")
        String cvv
) {}
