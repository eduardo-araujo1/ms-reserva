package com.eduardo.msreservation.client;

import com.eduardo.msreservation.dto.PropertyDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user-service", url = "${property-service.url}")
public interface PropertyClient {

    @GetMapping(value = "v1/properties/{propertyId}")
    PropertyDetailsDto getPropertyDetails(@PathVariable String propertyId);
}
