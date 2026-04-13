package com.brunofragadev.module.user.application.usecase;

import com.brunofragadev.module.user.api.dto.request.UserRegistrationRequest;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.domain.entity.Role;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.module.user.domain.event.GeneratedCodeEvent;
import com.brunofragadev.module.user.domain.repository.UserRepository;
import com.brunofragadev.module.user.application.validator.UserValidator;
import com.brunofragadev.module.user.application.mapper.UserMapper;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock private UserRepository userRepository;
    @Mock private UserValidator userValidator;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    private UserRegistrationRequest request;
    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        request = new UserRegistrationRequest("bruno", "bruno@email.com", "ellok", "bruno", "senha123");
        user = User.criar("bruno", "bruno", "bruno@email.com", "senha_criptografada", "bruno");
        userDTO = new UserDTO(
                1L, "bruno", "BRUNO", "bruno", false, "BRUNO@EMAIL.COM",
                Role.USER, false, null, null, null, null, null, null, null, null
        );
    }

    @Test
    @DisplayName("Deve registrar um usuário com sucesso e disparar o evento de código gerado")
    void shouldRegisterUserWithSuccess() {

        when(passwordEncoder.encode(request.senha())).thenReturn("senha_criptografada");
        when(userMapper.toNewUser(any(UserRegistrationRequest.class), anyString())).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);


        UserDTO result = registerUserUseCase.execute(request);


        assertNotNull(result);
        verify(userValidator).validateNewUser(request);
        verify(userRepository).save(user);


        verify(eventPublisher).publishEvent(any(GeneratedCodeEvent.class));
    }

    @Test
    @DisplayName("Deve interromper o fluxo quando a validação falhar")
    void shouldThrowExceptionWhenValidationFails() {
        doThrow(new RuntimeException("Email já cadastrado"))
                .when(userValidator).validateNewUser(request);

        assertThrows(RuntimeException.class, () -> registerUserUseCase.execute(request));

        verifyNoInteractions(userRepository);
        verifyNoInteractions(eventPublisher);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("Deve garantir que o evento contém o usuário correto")
    void shouldPublishEventWithCorrectData() {
        when(passwordEncoder.encode(anyString())).thenReturn("hash");
        when(userMapper.toNewUser(any(), any())).thenReturn(user);

        registerUserUseCase.execute(request);

        ArgumentCaptor<GeneratedCodeEvent> eventCaptor = ArgumentCaptor.forClass(GeneratedCodeEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        assertEquals(user, eventCaptor.getValue().user());
    }
}