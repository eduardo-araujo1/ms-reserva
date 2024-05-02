package com.eduardo.msuser.service;

import com.eduardo.msuser.converter.UserConverter;
import com.eduardo.msuser.dto.UserRequestDto;
import com.eduardo.msuser.dto.UserResponseDto;
import com.eduardo.msuser.exception.EmailAlreadyRegisteredException;
import com.eduardo.msuser.exception.UserNotFoundException;
import com.eduardo.msuser.model.User;
import com.eduardo.msuser.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        User userEntity = new User(UUID.randomUUID(), dto.name(), dto.email(), dto.password());

        when(converter.toModel(any(UserRequestDto.class))).thenReturn(userEntity);
        when(repository.save(any(User.class))).thenReturn(userEntity);
        when(converter.toDto(any(User.class))).thenReturn(new UserResponseDto(userEntity.getUserId(), userEntity.getName(), userEntity.getEmail()));

        var createdUser = service.create(dto);


        assertNotNull(createdUser);
        assertEquals(userEntity.getUserId(), createdUser.userId());
        assertEquals(userEntity.getEmail(), createdUser.email());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    public void userCreate_AlreadyRegistered(){
        UserRequestDto requestDto = new UserRequestDto("Teste", "teste@example.com", "123456");
        User existingUser = new User(UUID.randomUUID(), "Outro Usuário", "teste@example.com", "654321");

        when(repository.findByEmail(requestDto.email())).thenReturn(Optional.of(existingUser));

        assertThrows(EmailAlreadyRegisteredException.class, () -> service.create(requestDto));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    public void findUserByEmail(){
        String email = "teste@example.com";
        User userEntity = new User(UUID.randomUUID(), "Teste", email, "123456");

        when(repository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(converter.toDto(userEntity)).thenReturn(new UserResponseDto(userEntity.getUserId(), userEntity.getName(), userEntity.getEmail()));

        var foundUser = service.findUserByEmail(email);

        assertNotNull(foundUser);
        assertEquals(userEntity.getUserId(), foundUser.userId());
        assertEquals(userEntity.getEmail(), foundUser.email());
    }

    @Test
    public void findUserByEmail_NonExistingEmail_ThrowsRuntimeException() {
        String email = "naoexiste@example.com";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.findUserByEmail(email));
    }

    @Test
    public void testFindById() {
        String userId = UUID.randomUUID().toString();
        User existingUser = new User(UUID.fromString(userId),"Edu teste", "edu@teste.com","edu123");
        UserResponseDto expectedResponseDto = new UserResponseDto(existingUser.getUserId(),existingUser.getName(),existingUser.getEmail());

        when(repository.findById(UUID.fromString(userId))).thenReturn(Optional.of(existingUser));
        when(converter.toDto(existingUser)).thenReturn(expectedResponseDto);

        UserResponseDto foundUserDto = service.findById(userId);

        assertThat(foundUserDto).isEqualTo(expectedResponseDto);
    }

    @Test
    public void testFindById_UserNotFound(){
        String nonExistentUserId = UUID.randomUUID().toString();

        when(repository.findById(UUID.fromString(nonExistentUserId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(nonExistentUserId)).isInstanceOf(UserNotFoundException.class)
                    .hasMessage("Usuario não encontrado ou não existe.");

    }
}
