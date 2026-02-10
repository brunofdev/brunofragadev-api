package com.brunofragadev.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
    Optional<Usuario> findByUserName(String userName);
}
