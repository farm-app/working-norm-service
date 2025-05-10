package ru.rtln.workingnormservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.rtln.workingnormservice.config.ClientConfiguration;
import ru.rtln.workingnormservice.dto.UserResponse;

@FeignClient(name = "userApiClient", url = "${internal.service.user.url:http://localhost:9103/api/user}", configuration = ClientConfiguration.class)
public interface UserApiClient {

    @GetMapping(value = "/users/{userId}")
    ResponseEntity<UserResponse> getUserById(@PathVariable("userId") Long userId);

}