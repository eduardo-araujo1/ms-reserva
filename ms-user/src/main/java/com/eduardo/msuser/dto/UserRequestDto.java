package com.eduardo.msuser.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record UserRequestDto(
        @NotBlank
        @Size(min = 3, max = 100, message = "O nome deve conter no minimo 3 letras e no máximo 100.")
        String name,

        @Email(message = "Por favor, forneça um endereço de e-mail válido.")
        String email,
        @CPF(message = "Por favor, forneça um CPF válido.")
        String cpf,

        @Pattern(regexp = "\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}", message = "Por favor, forneça um número de telefone válido.")
        String phoneNumber,

        @Size(min = 5, max = 20, message = "A senha deve ter entre 8 e 20 caracteres.")
        String password
) {}
