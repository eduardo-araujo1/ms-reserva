package com.eduardo.msuser.service;

import com.eduardo.msuser.converter.UserConverter;
import com.eduardo.msuser.dto.UserRequestDto;
import com.eduardo.msuser.dto.UserResponseDto;
import com.eduardo.msuser.exception.EmailAlreadyRegisteredException;
import com.eduardo.msuser.exception.UserNotFoundException;
import com.eduardo.msuser.model.User;
import com.eduardo.msuser.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserConverter converter;

    public UserService(UserRepository repository, UserConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public UserResponseDto create(UserRequestDto requestDto){
        verifyIsEmailAlreadyRegistered(requestDto.email());
        User userInsert = converter.toModel(requestDto);
        User savedUser = repository.save(userInsert);
        return converter.toDto(savedUser);
    }

    public UserResponseDto findUserByEmail(String email){
        User findUser = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario não encontrado ou não existe."));
        return converter.toDto(findUser);
    }

    public UserResponseDto findById(String userId){
        User user = repository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("Usuario não encontrado ou não existe."));
        return converter.toDto(user);
    }

    private void verifyIsEmailAlreadyRegistered(String email){
        Optional<User> optionalUser = repository.findByEmail(email);
        if (optionalUser.isPresent()){
            throw new EmailAlreadyRegisteredException("Email já registrado.");
        }
    }


}
