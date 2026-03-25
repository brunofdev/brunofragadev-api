package com.brunofragadev.module.user.infrastructure.persistence;

import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.module.user.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository springRepository;

    public UserRepositoryAdapter(SpringDataUserRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public User save(User user) {
        return springRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return springRepository.findAll();
    }

    @Override
    public boolean existsByEmail(String email) {
        return springRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return springRepository.existsByUserName(userName);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return springRepository.findByUserName(userName);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUserNameOrEmail(String userName, String email) {
        return springRepository.findByUserNameOrEmail(userName, email);
    }
}