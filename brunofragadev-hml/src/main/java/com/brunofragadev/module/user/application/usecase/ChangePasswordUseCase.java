package com.brunofragadev.module.user.application.usecase;

import com.brunofragadev.infrastructure.email.EmailService;
import com.brunofragadev.module.user.api.dto.request.PasswordChangeRequest;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.module.user.domain.exception.UserEmailNotRegisteredException;
import com.brunofragadev.module.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public ChangePasswordUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void execute(PasswordChangeRequest request) {
        User user = userRepository.findByUserNameOrEmail(request.userName().toUpperCase(), request.userName().toUpperCase())
                .orElseThrow(() -> new UserEmailNotRegisteredException("Email or Username not found"));

        user.validarCodigo(request.codigoVerificado());

        String encodedPassword = passwordEncoder.encode(request.novaSenha());
        user.alterarSenha(encodedPassword);

        userRepository.save(user);

        emailService.sendPasswordChangedSuccessfullyEmail(user.getEmail(), user.getUsername());
    }
}