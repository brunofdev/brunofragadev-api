package com.brunofragadev.autenticacao.dto;


import com.brunofragadev.usuarios.entity.Role;

public record UserResponseDTO(String username, Role role) {
}
