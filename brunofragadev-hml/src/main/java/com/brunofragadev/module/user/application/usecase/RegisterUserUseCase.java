package com.brunofragadev.module.user.application.usecase;

import com.brunofragadev.infrastructure.email.EmailService;

import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.module.user.infrastructure.mapper.UserMapper;
import com.brunofragadev.module.user.domain.validator.UserValidator;
import com.brunofragadev.module.user.domain.repository.UserRepository;
import com.brunofragadev.module.user.api.dto.request.UserRegistrationRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public RegisterUserUseCase(UserRepository userRepository,
                               UserValidator userValidator,
                               UserMapper userMapper,
                               PasswordEncoder passwordEncoder,
                               EmailService emailService) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public UserDTO execute(UserRegistrationRequest request) {
        userValidator.validateNewUser(request);

        String encodedPassword = passwordEncoder.encode(request.senha());
        User user = userMapper.toNewUser(request, encodedPassword);

        String verificationCode = generateVerificationCode();
        user.definirCodigoVerificacao(verificationCode, LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);

        UserDTO userDTO = userMapper.toDTO(user);
        emailService.sendVerificationEmail(userDTO.email(), userDTO.userName(), verificationCode);

        return userDTO;
    }

    private String generateVerificationCode() {
        return String.format("%06d", new SecureRandom().nextInt(1_000_000));
    }
}