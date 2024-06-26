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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository repository;

    @Mock
    private UserClient userClient;

    @Mock
    private PropertyClient propertyClient;

    @Mock
    private ReservationConverter converter;

    @Mock
    private ReservationProducer producer;

    @InjectMocks
    private ReservationService service;

    @Test
    public void testCreateReservation() {
        ReservationRequestDto dto = new ReservationRequestDto("propertyId", "userId", LocalDate.now(), LocalDate.now().plusDays(2));
        PropertyInfoDto propertyInfoDto = new PropertyInfoDto("propertyId", BigDecimal.valueOf(250.0)); // Alterado para BigDecimal
        UserInfoDto userInfoDto = new UserInfoDto("userId", "Teste", "teste@email.com");
        Reservation entity = new Reservation(UUID.randomUUID(), dto.propertyId(), dto.userId(), dto.checkInDate(), dto.checkOutDate(), propertyInfoDto.pricePerNight(), EStatus.WAITING_PAYMENT, userInfoDto.email(),userInfoDto.name());
        ReservationResponseDto responseDto = new ReservationResponseDto(entity.getReservationId(), entity.getPropertyId(), entity.getUserId(), entity.getCheckInDate(), entity.getCheckOutDate(), entity.getTotalAmount(), entity.getStatus(), entity.getUserEmail(),entity.getUsername());

        when(propertyClient.getPropertyDetails(anyString())).thenReturn(propertyInfoDto);
        when(userClient.getUserDetails(anyString())).thenReturn(userInfoDto);
        when(converter.toModel(any(ReservationRequestDto.class))).thenReturn(entity);
        when(repository.save(any(Reservation.class))).thenReturn(entity);
        when(converter.toDto(any(Reservation.class))).thenReturn(responseDto);

        ReservationResponseDto result = service.createReservation(dto);

        assertNotNull(result);
        verify(repository).save(any(Reservation.class));
    }


    @Test
    public void testCreateReservation_InvalidDates_ThrowsException() {
        ReservationRequestDto dto = new ReservationRequestDto("propertyId", "userId", LocalDate.now(), LocalDate.now().minusDays(2));
        PropertyInfoDto propertyInfoDto = new PropertyInfoDto("propertyId", BigDecimal.valueOf(250.0));
        UserInfoDto userInfoDto = new UserInfoDto("userId", "Teste", "teste@email.com");
        Reservation entity = new Reservation(UUID.randomUUID(), dto.propertyId(), dto.userId(), dto.checkInDate(), dto.checkOutDate(), propertyInfoDto.pricePerNight(), EStatus.WAITING_PAYMENT, userInfoDto.email(),userInfoDto.name());

        when(propertyClient.getPropertyDetails(anyString())).thenReturn(propertyInfoDto);
        when(userClient.getUserDetails(anyString())).thenReturn(userInfoDto);
        when(converter.toModel(any(ReservationRequestDto.class))).thenReturn(entity);

        assertThrows(InvalidReservationPeriodException.class, () -> {
            service.createReservation(dto);
        });

        verify(repository, never()).save(any(Reservation.class));
    }


    @Test
    public void testCreateReservation_DateUnavailable_ThrowsException() {
        ReservationRequestDto dto = new ReservationRequestDto("propertyId", "userId", LocalDate.now(), LocalDate.now().plusDays(2));
        PropertyInfoDto propertyInfoDto = new PropertyInfoDto("propertyId", BigDecimal.valueOf(250.00));
        UserInfoDto userInfoDto = new UserInfoDto("userId", "Teste", "teste@email.com");


        when(propertyClient.getPropertyDetails(anyString())).thenReturn(propertyInfoDto);
        when(userClient.getUserDetails(anyString())).thenReturn(userInfoDto);
        when(converter.toModel(any(ReservationRequestDto.class))).thenReturn(new Reservation());

        List<Reservation> overlappingReservations = new ArrayList<>();
        overlappingReservations.add(new Reservation());
        when(repository.findOverlappingReservations(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(overlappingReservations);

        assertThrows(ReservationDateUnavailableException.class, () -> {
            service.createReservation(dto);
        });

        verify(repository, never()).save(any(Reservation.class));
    }

    @Test
    public void testCreateReservation_PropertyNotFound_ThrowsException() {
        ReservationRequestDto dto = new ReservationRequestDto("propertyId", "userId", LocalDate.now(), LocalDate.now().plusDays(2));

        when(propertyClient.getPropertyDetails("propertyId")).thenReturn(null);

        assertThrows(PropertyException.class, () -> service.createReservation(dto));

        verify(repository, never()).save(any(Reservation.class));
    }

    @Test
    public void testCreateReservation_UserNotFound_ThrowsException() {
        ReservationRequestDto dto = new ReservationRequestDto("propertyId", "userId", LocalDate.now(), LocalDate.now().plusDays(2));

        when(userClient.getUserDetails("userId")).thenReturn(null);
        when(propertyClient.getPropertyDetails("propertyId")).thenReturn(new PropertyInfoDto("propertyId", BigDecimal.valueOf(250.0))); // Alterado para BigDecimal

        assertThrows(UserException.class, () -> service.createReservation(dto));

        verify(repository, never()).save(any(Reservation.class));
    }

    @Test
    public void testFindReservationById(){
        String reservationId = UUID.randomUUID().toString();
        Reservation existingReservation = new Reservation(UUID.fromString(reservationId),"propertyId","userId", LocalDate.now(), LocalDate.now().plusDays(2), BigDecimal.valueOf(500.00), EStatus.WAITING_PAYMENT,"user@test.com","test123");
        ReservationResponseDto responseDto = new ReservationResponseDto(existingReservation.getReservationId(),existingReservation.getPropertyId(),existingReservation.getUserId(),existingReservation.getCheckInDate(),existingReservation.getCheckOutDate(),existingReservation.getTotalAmount(),existingReservation.getStatus(),existingReservation.getUserEmail(),existingReservation.getUsername());

        when(repository.findById(UUID.fromString(reservationId))).thenReturn(Optional.of(existingReservation));
        when(converter.toDto(existingReservation)).thenReturn(responseDto);

        ReservationResponseDto foundReservationDto = service.findByreservationId(reservationId);

        assertThat(foundReservationDto).isEqualTo(responseDto);
    }

    @Test
    public void testFindByReservationId_ReservationNotFoud(){
        String nonExistentReservationId = UUID.randomUUID().toString();

        when(repository.findById(UUID.fromString(nonExistentReservationId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByreservationId(nonExistentReservationId))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessage("Reserva não encontrada ou não existe.");
    }

    @Test
    void testProcessPayment_ReservationFoundAndPaymentValid_StatusUpdated() {
        String reservationId = UUID.randomUUID().toString();
        BigDecimal totalAmount = BigDecimal.valueOf(100.0);
        PaymentDto payment = new PaymentDto(totalAmount, "1234567890123456", "Edu teste", "12/25", "123");

        Reservation reservation = new Reservation(UUID.fromString(reservationId), "propertyId", "userId",
                LocalDate.now(), LocalDate.now().plusDays(2), totalAmount, EStatus.WAITING_PAYMENT,"user@test.com","test123");

        when(repository.findById(UUID.fromString(reservationId))).thenReturn(Optional.of(reservation));

        service.processPayment(reservationId, payment);

        assertEquals(EStatus.APPROVED, reservation.getStatus());
        verify(repository).findById(UUID.fromString(reservationId));
        verify(repository).save(reservation);
        verify(producer).publishMessageEmail(reservation);
    }

    @Test
    void testProcessPayment_ReservationNotFound_ExceptionThrown() {
        String reservationId = UUID.randomUUID().toString();
        BigDecimal totalAmount = BigDecimal.valueOf(100.00);
        PaymentDto payment = new PaymentDto(totalAmount, "1234567890123456", "Edu teste", "12/25", "123");

        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> service.processPayment(reservationId, payment));
    }

    @Test
    void testProcessPayment_InvalidPaymentAmount_ExceptionThrown() {
        String reservationId =UUID.randomUUID().toString();
        BigDecimal totalAmount = BigDecimal.valueOf(100.0); // Alterado para BigDecimal
        PaymentDto payment = new PaymentDto(BigDecimal.valueOf(50.00), "1234567890123456", "Edu teste", "12/25", "123");

        Reservation reservation = new Reservation(UUID.fromString(reservationId), "propertyId", "userId",
                LocalDate.now(), LocalDate.now().plusDays(2), totalAmount, EStatus.WAITING_PAYMENT,"user@test.com","test123");

        when(repository.findById(UUID.fromString(reservationId))).thenReturn(Optional.of(reservation));

        assertThrows(RuntimeException.class, () -> service.processPayment(reservationId, payment));
        assertEquals(EStatus.WAITING_PAYMENT, reservation.getStatus());
        verify(repository, never()).save(reservation);
    }
}
