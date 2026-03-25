package com.brunofragadev.module.user.application.usecase;

import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.domain.repository.UserRepository;
import com.brunofragadev.module.user.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListUsersUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ListUsersUseCase(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTO> execute() {
        return userMapper.toDTOList(userRepository.findAll());
    }
}