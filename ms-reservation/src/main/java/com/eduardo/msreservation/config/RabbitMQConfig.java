package com.eduardo.msreservation.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.email.exchange}")
    private String exchangeEmail;

    @Value("${rabbitmq.email.routingKey}")
    private String routingKey;

    @Bean
    public RabbitAdmin criarRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializarAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Queue createQueueNotificationEmail(){
        return QueueBuilder.durable("email.notification-service").build();
    }

    @Bean
    public DirectExchange createDirectExchangeEmailNotification(){
        return ExchangeBuilder.directExchange(exchangeEmail).build();
    }

    @Bean
    public Binding bindingEmailNotification(DirectExchange createDirectExchangeEmailNotification, Queue createQueueNotificationEmail) {
        return BindingBuilder.bind(createQueueNotificationEmail)
                .to(createDirectExchangeEmailNotification)
                .with(routingKey);
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());

        return rabbitTemplate;
    }
}
