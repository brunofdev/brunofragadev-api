package com.brunofragadev.usuarios.repository;

import com.brunofragadev.usuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
    Optional<Usuario> findByUserName(String userName);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUserNameOrEmail(String userName, String email);
}
