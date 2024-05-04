package com.eduardo.msreservation.service;

import com.eduardo.msreservation.client.PropertyClient;
import com.eduardo.msreservation.client.UserClient;
import com.eduardo.msreservation.client.dto.PropertyInfoDto;
import com.eduardo.msreservation.client.dto.UserInfoDto;
import com.eduardo.msreservation.converter.ReservationConverter;
import com.eduardo.msreservation.dto.PaymentDto;
import com.eduardo.msreservation.dto.ReservationRequestDto;
import com.eduardo.msreservation.dto.ReservationResponseDto;
import com.eduardo.msreservation.enums.EStatus;
import com.eduardo.msreservation.exception.*;
import com.eduardo.msreservation.model.Reservation;
import com.eduardo.msreservation.producer.ReservationProducer;
import com.eduardo.msreservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;
    private final UserClient userClient;
    private final PropertyClient propertyClient;
    private final ReservationConverter converter;
    private final ReservationProducer reservationProducer;

    public ReservationResponseDto createReservation(ReservationRequestDto dto) {
        PropertyInfoDto propertyDetails = getPropertyDetails(dto.propertyId());
        UserInfoDto userDetails = getUserDetails(dto.userId());

        Double totalAmount = calculateTotalAmount(propertyDetails, dto.checkInDate(), dto.checkOutDate());

        Reservation reservation = converter.toModel(dto);

        reservation.setTotalAmount(totalAmount);
        reservation.setStatus(EStatus.WAITING_PAYMENT);
        reservation.setUserEmail(userDetails.email());
        reservation.setUsername(userDetails.name());

        validateReservationPeriod(dto.checkInDate(), dto.checkOutDate());
        isDateAvailable(dto.checkInDate(), dto.checkOutDate(), dto.propertyId());

        Reservation savedReservation = repository.save(reservation);

        return converter.toDto(savedReservation);
    }

    public ReservationResponseDto findByreservationId(String reservationId) {
        Reservation findReservation = repository.findById(UUID.fromString(reservationId)).orElseThrow(
                () -> new ReservationNotFoundException("Reserva não encontrada ou não existe."));
        return converter.toDto(findReservation);
    }

    private void validateReservationPeriod(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkOutDate.isEqual(checkInDate) || checkOutDate.isBefore(checkInDate)) {
            throw new InvalidReservationPeriodException("A data de check-out deve ser posterior à data de check-in.");
        }
    }

    private void isDateAvailable(LocalDate checkInDate, LocalDate checkOutDate, String propertyId) {
        boolean dateAvailable = !repository.existsByCheckInDateAndCheckOutDateAndPropertyId(checkInDate, checkOutDate, propertyId);
        if (!dateAvailable) {
            throw new ReservationDateUnavailableException("Data de reserva indisponível. Já existe uma reserva para este período.");
        }
    }

    private PropertyInfoDto getPropertyDetails(String propertyId) {
        PropertyInfoDto propertyDetails = propertyClient.getPropertyDetails(propertyId);
        if (propertyDetails == null) {
            throw new PropertyException("Propriedade não encontrada: " + propertyId);
        }
        return propertyDetails;
    }

    private UserInfoDto getUserDetails(String userId) {
        UserInfoDto userDetails = userClient.getUserDetails(userId);
        if (userDetails == null) {
            throw new UserException("Usuário não encontrado: " + userId);
        }
        return userDetails;
    }

    private Double calculateTotalAmount(PropertyInfoDto propertyDetails, LocalDate checkInDate, LocalDate checkOutDate) {
        long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        double pricePerNight = propertyDetails.pricePerNight();
        double totalAmount = pricePerNight * numberOfNights;

        totalAmount = Math.round(totalAmount * 100.0) / 100.0;

        return totalAmount;
    }

    public void processPayment(String reservationId, PaymentDto payment) {
        Reservation reservation = repository.findById(UUID.fromString(reservationId))
                .orElseThrow(() -> new ReservationNotFoundException("Reserva não encontrada: " + reservationId));

        Double totalAmount = reservation.getTotalAmount();

        if (!payment.amount().equals(totalAmount)) {
            throw new RuntimeException("O valor do pagamento não corresponde ao valor total da reserva.");
        }

        reservation.setStatus(EStatus.APPROVED);
        reservationProducer.publishMessageEmail(reservation);

        repository.save(reservation);
    }
}
