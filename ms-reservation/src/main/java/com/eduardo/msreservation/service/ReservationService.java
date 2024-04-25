package com.eduardo.msreservation.service;

import com.eduardo.msreservation.client.PropertyClient;
import com.eduardo.msreservation.client.UserClient;
import com.eduardo.msreservation.client.dto.PropertyInfoDto;
import com.eduardo.msreservation.client.dto.UserInfoDto;
import com.eduardo.msreservation.converter.ReservationConverter;
import com.eduardo.msreservation.dto.ReservationRequestDto;
import com.eduardo.msreservation.dto.ReservationResponseDto;
import com.eduardo.msreservation.enums.EStatus;
import com.eduardo.msreservation.model.Reservation;
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

    public ReservationResponseDto createReservation(ReservationRequestDto dto) {
        PropertyInfoDto propertyDetails = getPropertyDetails(dto.propertyId());
        UserInfoDto userDetails = getUserDetails(dto.userId());
        Double totalAmount = calculateTotalAmount(propertyDetails, dto.checkinDate(), dto.checkOutDate());

        Reservation reservation = converter.toModel(dto);
        reservation.setTotalAmount(totalAmount);
        reservation.setStatus(EStatus.WAITING_PAYMENT);

        Reservation savedReservation = repository.save(reservation);

        return converter.toDto(savedReservation);
    }

    public ReservationResponseDto findByreservationId(String reservationId){
        Reservation findReservation = repository.findById(UUID.fromString(reservationId)).orElseThrow(
                () -> new RuntimeException("Reserva não encontrada ou não existe."));
        return converter.toDto(findReservation);
    }

    private PropertyInfoDto getPropertyDetails(String propertyId) {
        PropertyInfoDto propertyDetails = propertyClient.getPropertyDetails(propertyId);
        if (propertyDetails == null) {
            throw new RuntimeException("Propriedade não encontrada: " + propertyId);
        }
        return propertyDetails;
    }

    private UserInfoDto getUserDetails(String userId) {
        UserInfoDto userDetails = userClient.getUserDetails(userId);
        if (userDetails == null) {
            throw new RuntimeException("Usuário não encontrado: " + userId);
        }
        return userDetails;
    }

    private Double calculateTotalAmount(PropertyInfoDto propertyDetails, LocalDate checkInDate, LocalDate checkOutDate) {
        long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        double pricePerNight = Double.parseDouble(String.valueOf(propertyDetails.pricePerNight()));
        double totalAmount = pricePerNight * numberOfNights;

        totalAmount = Math.round(totalAmount * 100.0) / 100.0;

        return totalAmount;
    }


}
