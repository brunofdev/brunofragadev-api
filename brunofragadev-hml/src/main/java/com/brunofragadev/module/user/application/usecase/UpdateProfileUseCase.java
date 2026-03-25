package com.brunofragadev.module.user.application.usecase;

import com.brunofragadev.module.user.api.dto.request.UpdateProfileData;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.module.user.domain.exception.UserNotFoundException;
import com.brunofragadev.module.user.domain.repository.UserRepository;
import com.brunofragadev.module.user.infrastructure.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UpdateProfileUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UpdateProfileUseCase(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDTO execute(String userName, UpdateProfileData updateData) {
        User user = findUserByUserName(userName);

        User updatedUser = userMapper.updateEntityFromData(user, updateData); // Lembre-se de renomear no Mapper
        userRepository.save(updatedUser);

        return userMapper.toDTO(updatedUser);
    }

    private User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName.toUpperCase())
                .orElseThrow(() -> new UserNotFoundException("Username not found"));
    }
}