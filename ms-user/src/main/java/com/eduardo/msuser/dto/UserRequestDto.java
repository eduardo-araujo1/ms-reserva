package com.eduardo.msuser.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record UserRequestDto(
        @NotBlank
        @Size(min = 3, max = 100, message = "O nome deve conter no minimo 3 letras e no máximo 100.")
        String name,

        @Email(message = "Por favor, forneça um endereço de e-mail válido.")
        String email,

        @Size(min = 5, max = 20, message = "A senha deve ter entre 8 e 20 caracteres.")
        String password
) {}
