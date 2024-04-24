package com.eduardo.msuser.converter;

import com.eduardo.msuser.dto.UserRequestDto;
import com.eduardo.msuser.dto.UserResponseDto;
import com.eduardo.msuser.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User toModel(UserRequestDto dto){
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        return user;
    }

    public UserResponseDto toDto(User user){
        return new UserResponseDto(
                user.getUserId(),
                user.getName(),
                user.getEmail()
        );
    }
}
