package com.brunofragadev.module.user.api.dto.response;

import com.brunofragadev.module.user.domain.entity.Role;

public record UserDTO(
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
