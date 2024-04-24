package com.eduardo.msreservation.controller;

import com.eduardo.msreservation.dto.ReservationRequestDto;
import com.eduardo.msreservation.dto.ReservationResponseDto;
import com.eduardo.msreservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto requestDto) {
        var created = service.createReservation(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/v1/reservations/{reservationId}")
    public ResponseEntity<Void> getReservationStatus(@PathVariable UUID reservationId) {
       return null; // Lógica para obter o status da reserva com o ID fornecido e retornar uma resposta adequada
    }
}
