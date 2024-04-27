package com.eduardo.msreservation.controller;

import com.eduardo.msreservation.dto.ReservationRequestDto;
import com.eduardo.msreservation.dto.ReservationResponseDto;
import com.eduardo.msreservation.service.ReservationService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ReservationResponseDto> createReservation(@Valid @RequestBody ReservationRequestDto requestDto) {
        var created = service.createReservation(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping(params = ("reservationId"))
    public ResponseEntity<ReservationResponseDto> getReservationStatus(@RequestParam("reservationId") String reservationId) {
       var findReservation = service.findByreservationId(reservationId);
       return ResponseEntity.ok().body(findReservation);
    }


}
