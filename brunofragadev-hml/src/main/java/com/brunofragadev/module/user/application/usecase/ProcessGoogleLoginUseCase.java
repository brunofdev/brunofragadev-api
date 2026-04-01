package com.brunofragadev.module.user.application.usecase;

import com.brunofragadev.infrastructure.email.EmailService;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.module.user.domain.repository.UserRepository;
import com.brunofragadev.module.user.application.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProcessGoogleLoginUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public ProcessGoogleLoginUseCase(UserRepository userRepository, UserMapper userMapper,
                                     PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public UserDTO execute(String email, String name, String photoUrl) {
        String formattedEmail = email.toUpperCase();
        Optional<User> existingUser = userRepository.findByEmail(formattedEmail);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (photoUrl != null && !photoUrl.equals(user.getFotoperfil())) {
                user.setFotoperfil(photoUrl);
            }
            if (!user.isContaAtiva()) {
                user.ativarConta();
            }
            return userMapper.toDTO(user);
        }

        String finalUserName = email.split("@")[0].toUpperCase();
        if (userRepository.existsByUserName(finalUserName)) {
            finalUserName = finalUserName + "_" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        }

        String encodedPassword = passwordEncoder.encode(UUID.randomUUID().toString());

        User newUser = userMapper.toNewGoogleUser(
                formattedEmail, name, photoUrl, encodedPassword, finalUserName
        );

        userRepository.save(newUser);

        UserDTO newUserDTO = userMapper.toDTO(newUser);
        emailService.sendWelcomeEmail(newUserDTO);

        return newUserDTO;
    }
}