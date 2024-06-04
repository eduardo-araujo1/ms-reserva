package com.eduardo.msreservation.repository;

import com.eduardo.msreservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {


    @Query("SELECT r FROM Reservation r " +
            "WHERE r.propertyId = :propertyId " +
            "AND (:checkInDate < r.checkOutDate AND :checkOutDate > r.checkInDate)")
    List<Reservation> findOverlappingReservations(@Param("propertyId") String propertyId,
                                                  @Param("checkInDate") LocalDate checkInDate,
                                                  @Param("checkOutDate") LocalDate checkOutDate);



}
