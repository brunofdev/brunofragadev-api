package com.brunofragadev.module.user.application.usecase;

import com.brunofragadev.infrastructure.email.EmailService;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.module.user.domain.exception.UserNotFoundException;
import com.brunofragadev.module.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class GenerateVerificationCodeUseCase {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public GenerateVerificationCodeUseCase(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void execute(String userName) {
        User user = userRepository.findByUserName(userName.toUpperCase())
                .orElseThrow(() -> new UserNotFoundException("Username not found"));

        String verificationCode = generateVerificationCode();
        user.definirCodigoVerificacao(verificationCode, LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), user.getUsername(), verificationCode);
    }

    private String generateVerificationCode() {
        return String.format("%06d", new SecureRandom().nextInt(1_000_000));
    }
}