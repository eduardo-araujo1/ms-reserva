package com.eduardo.gatewayservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Routes {

    @Value("${user-service.url}")
    private String userServiceUrl;

    @Value("${property-service.url}")
    private String propertyServiceUrl;

    @Value("${reservation-service.url}")
    private String reservationServiceUrl;

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("ms-user", r -> r.path("/v1/users/**")
                        .uri(userServiceUrl))
                .route("ms-property-catalog", r -> r.path("/v1/properties/**")
                        .uri(propertyServiceUrl))
                .route("ms-reservation", r -> r.path("/v1/reservations/**")
                        .uri(reservationServiceUrl))
                .build();
    }
}
