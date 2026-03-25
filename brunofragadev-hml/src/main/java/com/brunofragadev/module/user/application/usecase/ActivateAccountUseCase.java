package com.brunofragadev.module.user.application.usecase;

import com.brunofragadev.infrastructure.email.EmailService;
import com.brunofragadev.module.user.api.dto.request.UserValidationRequest;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.module.user.domain.exception.UserEmailNotRegisteredException;
import com.brunofragadev.module.user.domain.repository.UserRepository;
import com.brunofragadev.module.user.infrastructure.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ActivateAccountUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;

    public ActivateAccountUseCase(UserRepository userRepository, UserMapper userMapper, EmailService emailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.emailService = emailService;
    }

    @Transactional
    public UserDTO execute(UserValidationRequest request) {
        User user = userRepository.findByUserNameOrEmail(request.userName().toUpperCase(), request.userName().toUpperCase())
                .orElseThrow(() -> new UserEmailNotRegisteredException("Email or Username not found"));

        user.validarCodigo(request.codigo());
        user.ativarConta();

        userRepository.save(user);

        UserDTO userDTO = userMapper.toDTO(user); // Lembre-se de renomear no seu Mapper
        emailService.sendWelcomeEmail(userDTO); // Lembre-se de renomear no seu EmailService

        return userDTO;
    }
}