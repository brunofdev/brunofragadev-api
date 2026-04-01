package com.brunofragadev.module.user.application.usecase;

import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.module.user.domain.exception.InvalidCredentialsException;
import com.brunofragadev.module.user.domain.exception.UserEmailNotRegisteredException;
import com.brunofragadev.module.user.domain.repository.UserRepository;
import com.brunofragadev.module.user.application.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ValidateCredentialsUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public ValidateCredentialsUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserDTO execute(String userNameOrEmail, String rawPassword) {
        User user = userRepository.findByUserNameOrEmail(userNameOrEmail.toUpperCase(), userNameOrEmail.toUpperCase())
                .orElseThrow(() -> new UserEmailNotRegisteredException("Email or Username not found"));

        if (!passwordEncoder.matches(rawPassword, user.getSenha())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return userMapper.toDTO(user);
    }
}