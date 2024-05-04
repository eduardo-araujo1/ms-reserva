package com.eduardo.notificationservice.dto;

import java.util.UUID;

public record EmailDto(
        UUID reservationId,
        String emailTo,
        String subject,
        String text
) {
}
