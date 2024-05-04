package com.eduardo.msreservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EmailDto {

    private UUID reservationId;
    private String emailTo;
    private String subject;
    private String text;
}
