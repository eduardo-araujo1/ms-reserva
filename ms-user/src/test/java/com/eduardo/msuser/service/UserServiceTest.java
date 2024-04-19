package com.eduardo.msuser.service;

import com.eduardo.msuser.converter.UserConverter;
import com.eduardo.msuser.dto.UserRequestDto;
import com.eduardo.msuser.dto.UserResponseDto;
import com.eduardo.msuser.exception.EmailAlreadyRegisteredException;
import com.eduardo.msuser.exception.EmailNotFoundException;
import com.eduardo.msuser.model.User;
import com.eduardo.msuser.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserConverter converter;

    @InjectMocks
    private UserService service;

    @Test
    public void createUser(){
        UserRequestDto dto = new UserRequestDto("Teste","teste@example.com","123456");
        User userEntity = new User(UUID.randomUUID(), dto.getName(), dto.getEmail(), dto.getPassword());

        when(converter.toModel(any(UserRequestDto.class))).thenReturn(userEntity);
        when(repository.save(any(User.class))).thenReturn(userEntity);
        when(converter.toDto(any(User.class))).thenReturn(new UserResponseDto(userEntity.getId(), userEntity.getName(), userEntity.getEmail()));

        var createdUser = service.create(dto);


        assertNotNull(createdUser);
        assertEquals(userEntity.getId(), createdUser.getId());
        assertEquals(userEntity.getEmail(), createdUser.getEmail());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    public void userCreate_AlreadyRegistered(){
        UserRequestDto requestDto = new UserRequestDto("Teste", "teste@example.com", "123456");
        User existingUser = new User(UUID.randomUUID(), "Outro Usuário", "teste@example.com", "654321");

        when(repository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(EmailAlreadyRegisteredException.class, () -> service.create(requestDto));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    public void findUserByEmail(){
        String email = "teste@example.com";
        User userEntity = new User(UUID.randomUUID(), "Teste", email, "123456");

        when(repository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(converter.toDto(userEntity)).thenReturn(new UserResponseDto(userEntity.getId(), userEntity.getName(), userEntity.getEmail()));

        var foundUser = service.findUserByEmail(email);

        assertNotNull(foundUser);
        assertEquals(userEntity.getId(), foundUser.getId());
        assertEquals(userEntity.getEmail(), foundUser.getEmail());
    }

    @Test
    public void findUserByEmail_NonExistingEmail_ThrowsRuntimeException() {
        String email = "naoexiste@example.com";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> service.findUserByEmail(email));
    }
}
