package com.eduardo.msreservation.repository;

import com.eduardo.msreservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    Boolean existsByCheckInDateAndCheckOutDateAndPropertyId(LocalDate checkInDate, LocalDate checkOutDate, String propertyId);


}
