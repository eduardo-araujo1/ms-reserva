package com.eduardo.msreservation.client;

import com.eduardo.msreservation.client.dto.UserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ms-user", url = "${user-service.url}")
public interface UserClient {

    @GetMapping(value = "v1/users", params = "userId")
    UserInfoDto getUserDetails(@RequestParam("userId") String userId);

}
