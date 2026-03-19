package com.brunofragadev.autenticacao.dto;

import jakarta.validation.constraints.NotBlank;

public record CredenciaisDTO(
        @NotBlank(message = "O nome de usuario ou email não pode estar em branco.")
        String userName,
        @NotBlank(message = "Senha não pode estar em branco.")
        String senha
) {
}
