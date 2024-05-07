package com.eduardo.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class Routes {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("ms-user", r -> r.path("/v1/users/**")
                        .uri("http://localhost:8081"))
                .route("ms-property-catalog", r -> r.path("/v1/properties/**")
                        .uri("http://localhost:8082"))
                .route("ms-reservation", r -> r.path("/v1/reservations/**")
                        .uri("http://localhost:8083"))
                .build();
    }
}
