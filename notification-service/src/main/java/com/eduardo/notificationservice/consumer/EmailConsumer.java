package com.eduardo.notificationservice.consumer;

import com.eduardo.notificationservice.dto.EmailDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    @RabbitListener(queues = "${queue.name}")
    public void listenEmailQueue(@Payload EmailDto emailDto){
        System.out.println(emailDto.emailTo());
    }
}
