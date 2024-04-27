package com.eduardo.msreservation.service;

import com.eduardo.msreservation.client.PropertyClient;
import com.eduardo.msreservation.client.UserClient;
import com.eduardo.msreservation.client.dto.PropertyInfoDto;
import com.eduardo.msreservation.client.dto.UserInfoDto;
import com.eduardo.msreservation.converter.ReservationConverter;
import com.eduardo.msreservation.dto.ReservationRequestDto;
import com.eduardo.msreservation.dto.ReservationResponseDto;
import com.eduardo.msreservation.enums.EStatus;
import com.eduardo.msreservation.exception.*;
import com.eduardo.msreservation.model.Reservation;
import com.eduardo.msreservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;
    private final UserClient userClient;
    private final PropertyClient propertyClient;
    private final ReservationConverter converter;

    public ReservationResponseDto createReservation(ReservationRequestDto dto) {
        PropertyInfoDto propertyDetails = getPropertyDetails(dto.propertyId());
        UserInfoDto userDetails = getUserDetails(dto.userId());

        Double totalAmount = calculateTotalAmount(propertyDetails, dto.checkInDate(), dto.checkOutDate());

        Reservation reservation = converter.toModel(dto);
        reservation.setTotalAmount(totalAmount);
        reservation.setStatus(EStatus.WAITING_PAYMENT);

        validateReservationPeriod(dto);
        isDateAvailable(dto.checkInDate(), dto.checkOutDate(), dto.propertyId());

        Reservation savedReservation = repository.save(reservation);

        return converter.toDto(savedReservation);
    }

    private void validateReservationPeriod(ReservationRequestDto dto) {
        if (!dto.isValidReservationPeriod()) {
            throw new InvalidReservationPeriodException("A data de check-out deve ser posterior à data de check-in.");
        }
    }

    private boolean isDateAvailable(LocalDate checkInDate, LocalDate checkOutDate, String propertyId) {
        boolean dateAvailable = !repository.existsByCheckInDateAndCheckOutDateAndPropertyId(checkInDate, checkOutDate, propertyId);
        if (!dateAvailable) {
            throw new ReservationDateUnavailableException("Data de reserva indisponível. Já existe uma reserva para este período.");
        }
        return dateAvailable;
    }

    public ReservationResponseDto findByreservationId(String reservationId){
        Reservation findReservation = repository.findById(UUID.fromString(reservationId)).orElseThrow(
                () -> new ReservationNotFoundException("Reserva não encontrada ou não existe."));
        return converter.toDto(findReservation);
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
        Long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        Double pricePerNight = Double.parseDouble(String.valueOf(propertyDetails.pricePerNight()));
        double totalAmount = pricePerNight * numberOfNights;

        totalAmount = Math.round(totalAmount * 100.0) / 100.0;

        return totalAmount;
    }
}
