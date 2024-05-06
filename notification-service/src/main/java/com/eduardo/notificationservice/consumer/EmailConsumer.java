package com.eduardo.notificationservice.consumer;

import com.eduardo.notificationservice.dto.EmailDto;
import com.eduardo.notificationservice.dto.EmailModel;
import com.eduardo.notificationservice.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

     final EmailService service;

    public EmailConsumer(EmailService service) {
        this.service = service;
    }

    @RabbitListener(queues = "${queue.email-notification}")
    public void listenEmailQueue(@Payload EmailDto emailDto){
        var emailModel = new EmailModel();
        BeanUtils.copyProperties(emailDto, emailModel);
        service.sendEmail(emailModel);
    }
}
