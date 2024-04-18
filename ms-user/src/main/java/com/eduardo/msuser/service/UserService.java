package com.eduardo.msuser.service;

import com.eduardo.msuser.converter.UserConverter;
import com.eduardo.msuser.dto.UserRequestDto;
import com.eduardo.msuser.dto.UserResponseDto;
import com.eduardo.msuser.model.User;
import com.eduardo.msuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserConverter converter;

    public UserResponseDto create(UserRequestDto requestDto){
        verifyIsEmailAlreadyRegistered(requestDto.getEmail());
        User userInsert = converter.toModel(requestDto);
        User savedUser = repository.save(userInsert);
        return converter.toDto(savedUser);

    }

    private void verifyIsEmailAlreadyRegistered(String email){
        Optional<User> optionalUser = repository.findByEmail(email);
        if (optionalUser.isPresent()){
            throw new RuntimeException("Email já registrado");
        }
    }
}
