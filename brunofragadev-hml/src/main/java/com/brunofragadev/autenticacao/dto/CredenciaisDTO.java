package com.brunofragadev.autenticacao.dto;

import jakarta.validation.constraints.NotBlank;

public record CredenciaisDTO(
        @NotBlank(message = "O cpf não pode estar em branco.")
        String cpf,
        @NotBlank(message = "Senha não pode estar em branco.")
        String senha) {
}
