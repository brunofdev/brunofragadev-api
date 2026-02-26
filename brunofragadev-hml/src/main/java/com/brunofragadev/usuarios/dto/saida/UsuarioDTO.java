package com.brunofragadev.usuarios.dto.saida;

import com.brunofragadev.usuarios.entity.Role;

public record UsuarioDTO(
        Long id,
        String nome,
        String userName,
        String nomePublico,
        Boolean isAnonimo,
        String email,
        Role role,
        Boolean contaAtiva,
        String cidade,
        String gitHub,
        String profissao,
        String bio,
        String fotoPerfil,
        String linkedin,
        String pais,
        String telefone

){
}
