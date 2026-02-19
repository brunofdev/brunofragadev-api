package com.brunofragadev.autenticacao.dto;


import com.brunofragadev.usuarios.Role;

public record UserResponseDTO(String username, Role role) {
}
