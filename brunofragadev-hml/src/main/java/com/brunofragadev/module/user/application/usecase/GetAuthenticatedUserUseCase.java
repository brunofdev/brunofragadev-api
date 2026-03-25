package com.brunofragadev.module.user.application.usecase;

import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.module.user.domain.exception.UserNotFoundException;
import com.brunofragadev.module.user.domain.repository.UserRepository;
import com.brunofragadev.module.user.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class GetAuthenticatedUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public GetAuthenticatedUserUseCase(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO execute(String userName) {
        User user = userRepository.findByUserName(userName.toUpperCase())
                .orElseThrow(() -> new UserNotFoundException("Username not found"));

        return userMapper.toDTO(user);
    }
}