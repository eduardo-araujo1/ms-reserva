package com.eduardo.msreservation.repository;

import com.eduardo.msreservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
}
