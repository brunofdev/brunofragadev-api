package com.brunofragadev.usuarios;

public record UsuarioDTO(
        Long id,
        String nome,
        String userName,
        String email,
        Role role,
        Boolean contaAtiva
){
}
