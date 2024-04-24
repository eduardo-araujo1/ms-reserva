package com.eduardo.msreservation.client;

import com.eduardo.msreservation.dto.UserDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(value = "user-service", url = "${user-service.url}")
public interface UserClient {

    @GetMapping(value = "/v1/users/{userId}")
    UserDetailsDto getUserDetails(@PathVariable String userId);


}
