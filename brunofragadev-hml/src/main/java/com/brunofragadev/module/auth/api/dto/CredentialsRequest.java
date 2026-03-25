package com.brunofragadev.module.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CredentialsRequest(
        @Schema(description = "Nome de usuário ou email para login", example = "brunodev or bruno@email.com")
        @NotBlank(message = "O nome de usuário ou email não pode estar em branco.")
        String userName,
        @NotBlank(message = "Senha não pode estar em branco.")
        String senha
) {
}
