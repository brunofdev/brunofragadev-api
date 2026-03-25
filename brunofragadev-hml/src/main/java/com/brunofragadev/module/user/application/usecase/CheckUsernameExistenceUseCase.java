package com.brunofragadev.module.user.application.usecase;

import com.brunofragadev.module.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CheckUsernameExistenceUseCase {

    private final UserRepository userRepository;

    public CheckUsernameExistenceUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean execute(String userName) {
        return userRepository.existsByUserName(userName.toUpperCase());
    }
}