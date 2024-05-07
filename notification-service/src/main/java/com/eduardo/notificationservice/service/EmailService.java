package com.eduardo.notificationservice.service;

import com.eduardo.notificationservice.dto.EmailModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.eduardo.notificationservice.enums.StatusEmail.ERROR;
import static com.eduardo.notificationservice.enums.StatusEmail.SENT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(EmailModel emailModel) {
        try {
            emailModel.setSendDateEmail(LocalDateTime.now());
            emailModel.setEmailFrom(emailFrom);

            logger.info("Enviando e-mail para: {}", emailModel.getEmailTo());
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());
            emailSender.send(message);
            logger.info("E-mail enviado com sucesso!");

            emailModel.setStatusEmail(SENT);
        } catch (MailException e) {
            logger.error("Erro ao enviar e-mail: {}", e.getMessage());
            emailModel.setStatusEmail(ERROR);
        }
    }
}