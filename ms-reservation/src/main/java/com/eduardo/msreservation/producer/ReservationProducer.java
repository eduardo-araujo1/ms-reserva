package com.eduardo.msreservation.producer;

import com.eduardo.msreservation.dto.EmailDto;
import com.eduardo.msreservation.model.Reservation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReservationProducer {

    final RabbitTemplate rabbitTemplate;

    public ReservationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${rabbitmq.email.exchange}")
    private String exchange;

    @Value("${rabbitmq.email.routingKey}")
    private String routingKey;

    public void publishMessageEmail(Reservation reservation) {
        var emailDto = new EmailDto();
        emailDto.setReservationId(reservation.getReservationId());
        emailDto.setEmailTo(reservation.getUserEmail());
        emailDto.setSubject("Pagamento realizado com sucesso!");

        String message = "Olá " + reservation.getUsername() + ",\n\n" +
                "Gostaríamos de informar que o pagamento para a reserva com o ID " +
                reservation.getReservationId() + " foi realizado com sucesso!\n\n" +
                "Detalhes do Pedido:\n" +
                "- Data de CheckIn: " + reservation.getCheckInDate() + "\n" +
                "- Data de CheckOut: " + reservation.getCheckOutDate() + "\n" +
                "- Valor Total: " + reservation.getTotalAmount() + "\n" +
                "\n\nObrigado por escolher nosso serviço. Se precisar de mais alguma informação, " +
                "não hesite em nos contatar.";

        emailDto.setText(message);

        rabbitTemplate.convertAndSend(exchange,routingKey ,emailDto);
    }

}
