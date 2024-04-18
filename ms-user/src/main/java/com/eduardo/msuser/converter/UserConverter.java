package com.eduardo.msuser.converter;

import com.eduardo.msuser.dto.UserRequestDto;
import com.eduardo.msuser.dto.UserResponseDto;
import com.eduardo.msuser.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User toModel(UserRequestDto dto){
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    public UserResponseDto toDto(User user){
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
