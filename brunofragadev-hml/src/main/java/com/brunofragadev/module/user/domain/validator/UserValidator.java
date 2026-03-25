package com.brunofragadev.module.user.domain.validator;

import com.brunofragadev.module.user.api.dto.request.UpdateProfileData;
import com.brunofragadev.module.user.api.dto.request.UserRegistrationRequest;
import com.brunofragadev.module.user.domain.exception.EmailAlreadyExistsException;
import com.brunofragadev.module.user.domain.exception.UsernameAlreadyExistsException;
import com.brunofragadev.module.user.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void validateNewUser(UserRegistrationRequest request) {
        checkEmailUniqueness(request.email());
        checkUsernameUniqueness(request.userName());
    }


    private void checkEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already registered in the system");
        }
    }

    private void checkUsernameUniqueness(String userName) {
        if (userRepository.existsByUserName(userName)) {
            throw new UsernameAlreadyExistsException("Username already registered in the system");
        }
    }
}