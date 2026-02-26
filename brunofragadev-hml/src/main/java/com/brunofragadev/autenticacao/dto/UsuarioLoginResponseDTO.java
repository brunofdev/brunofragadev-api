package com.brunofragadev.autenticacao.dto;


import com.brunofragadev.usuarios.dto.saida.UsuarioDTO;

public record UsuarioLoginResponseDTO(String token, UsuarioDTO clienteDTO) {
}
