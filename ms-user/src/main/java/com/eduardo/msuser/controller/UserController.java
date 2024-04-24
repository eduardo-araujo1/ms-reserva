package com.eduardo.msuser.controller;

import com.eduardo.msuser.dto.UserRequestDto;
import com.eduardo.msuser.dto.UserResponseDto;
import com.eduardo.msuser.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto dto){
        var userDto = service.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{name}")
                .buildAndExpand(dto.name()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> findUserByEmail(@RequestParam String email){
        var findUser = service.findUserByEmail(email);
        return ResponseEntity.ok().body(findUser);
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable String userId){
        UserResponseDto findById = service.findById(userId);
        return ResponseEntity.ok().body(findById);
    }
}
