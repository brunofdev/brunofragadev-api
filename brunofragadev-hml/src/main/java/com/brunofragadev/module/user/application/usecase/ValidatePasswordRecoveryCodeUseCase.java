package com.brunofragadev.module.user.application.usecase;

import com.brunofragadev.module.user.api.dto.request.UserValidationRequest;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.module.user.domain.exception.UserEmailNotRegisteredException;
import com.brunofragadev.module.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ValidatePasswordRecoveryCodeUseCase {

    private final UserRepository userRepository;

    public ValidatePasswordRecoveryCodeUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(UserValidationRequest request) {
        User user = userRepository.findByUserNameOrEmail(request.userName().toUpperCase(), request.userName().toUpperCase())
                .orElseThrow(() -> new UserEmailNotRegisteredException("Email or Username not found"));

        user.validarCodigo(request.codigo());
    }
}