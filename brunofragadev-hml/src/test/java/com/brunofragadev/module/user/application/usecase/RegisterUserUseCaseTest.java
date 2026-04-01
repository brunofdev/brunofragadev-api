package com.brunofragadev.module.user.application.usecase;

import com.brunofragadev.infrastructure.email.EmailService;
import com.brunofragadev.module.user.api.dto.request.UserRegistrationRequest;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.domain.entity.Role;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.module.user.domain.repository.UserRepository;
import com.brunofragadev.module.user.domain.validator.UserValidator;
import com.brunofragadev.module.user.application.mapper.UserMapper;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock private UserRepository userRepository;
    @Mock private UserValidator userValidator;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EmailService emailService;

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
    @DisplayName("Deve registrar um usuário com sucesso quando os dados forem válidos")
    void shouldRegisterUserWithSuccess() {
        when(passwordEncoder.encode(request.senha())).thenReturn("senha_criptografada");
        when(userMapper.toNewUser(any(UserRegistrationRequest.class), anyString())).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = registerUserUseCase.execute(request);

        assertNotNull(result);
        assertEquals("BRUNO@EMAIL.COM", result.email());


        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertNotNull(userCaptor.getValue().getVerificationCode(),
                "O código de verificação deve ser definido antes de salvar");

        verify(userValidator).validateNewUser(request);
        verify(emailService).sendVerificationEmail(eq("BRUNO@EMAIL.COM"), anyString(), anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção e interromper o fluxo quando a validação falhar")
    void shouldThrowExceptionWhenValidationFails() {
        doThrow(new RuntimeException("Email já cadastrado"))
                .when(userValidator).validateNewUser(request);

        assertThrows(RuntimeException.class, () -> registerUserUseCase.execute(request));

        verifyNoInteractions(userRepository);
        verifyNoInteractions(emailService);
        verifyNoInteractions(userMapper);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o envio de email falhar")
    void shouldThrowExceptionWhenEmailServiceFails() {
        when(passwordEncoder.encode(request.senha())).thenReturn("senha_criptografada");
        when(userMapper.toNewUser(any(UserRegistrationRequest.class), anyString())).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);
        doThrow(new RuntimeException("Falha ao enviar email"))
                .when(emailService).sendVerificationEmail(anyString(), anyString(), anyString());

        assertThrows(RuntimeException.class, () -> registerUserUseCase.execute(request));
        verify(userRepository).save(any(User.class));
    }
}