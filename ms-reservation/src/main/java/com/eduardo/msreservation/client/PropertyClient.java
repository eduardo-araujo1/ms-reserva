package com.eduardo.msreservation.client;

import com.eduardo.msreservation.client.dto.PropertyInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ms-property-catalog", url = "${property-service.url}")
public interface PropertyClient {

    @GetMapping(value = "/v1/properties", params = "propertyId")
    PropertyInfoDto getPropertyDetails(@RequestParam("propertyId") String propertyId);
}

