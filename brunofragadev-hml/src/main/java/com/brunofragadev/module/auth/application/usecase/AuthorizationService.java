package com.brunofragadev.module.auth.application.usecase;

import com.brunofragadev.module.user.domain.repository.UserRepository;
import com.brunofragadev.module.user.domain.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UserNotFoundException {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("No user found with the provided username in the token"));
    }
}