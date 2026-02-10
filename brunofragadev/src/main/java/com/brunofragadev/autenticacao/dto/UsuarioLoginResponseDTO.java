package com.brunofragadev.autenticacao.dto;


import com.brunofragadev.usuarios.UsuarioDTO;

public record UsuarioLoginResponseDTO(String token, UsuarioDTO clienteDTO) {
}
