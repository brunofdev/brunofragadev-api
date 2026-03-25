package com.brunofragadev.module.user.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para autenticação de usuário")
public record UserValidationRequest(
        @Schema(description = "Nome de usuário ou email para login", example = "brunodev or bruno@email.com")
        @NotBlank(message = "O nome de usuário ou email não pode estar em branco.")
        String userName,

        @Schema(description = "Código de autenticação", example = "123456")
        @NotBlank(message = "O código não pode estar em branco.")
        String codigo
){
}