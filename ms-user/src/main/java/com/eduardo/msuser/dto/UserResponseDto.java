package com.eduardo.msuser.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

public record UserResponseDto(
        UUID userId,
        String name,
        String email
) {}