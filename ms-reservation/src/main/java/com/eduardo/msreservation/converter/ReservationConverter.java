package com.eduardo.msreservation.converter;

import com.eduardo.msreservation.dto.ReservationRequestDto;
import com.eduardo.msreservation.dto.ReservationResponseDto;
import com.eduardo.msreservation.model.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationConverter {

    public Reservation toModel(ReservationRequestDto dto){
        Reservation reservation = new Reservation();
        reservation.setPropertyId(dto.propertyId());
        reservation.setUserId(dto.userId());
        reservation.setCheckInDate(dto.checkInDate());
        reservation.setCheckOutDate(dto.checkOutDate());
        return reservation;
    }

    public ReservationResponseDto toDto(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getReservationId(),
                reservation.getPropertyId(),
                reservation.getUserId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getTotalAmount(),
                reservation.getStatus()
        );
    }
}
