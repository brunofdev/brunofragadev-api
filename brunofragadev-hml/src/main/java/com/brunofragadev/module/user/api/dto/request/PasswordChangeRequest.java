package com.brunofragadev.module.user.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record PasswordChangeRequest(


        @Schema(description = "Nome de usuário ou email para login", example = "brunodev or bruno@email.com")
        @NotBlank(message = "O nome de usuário ou email não pode estar em branco.")
        String userName,

        @Schema(description = "Senha forte (min 8 chars, 1 maiúscula, 1 minúscula, 1 número, 1 especial)", example = "Senha@123")
        @NotBlank(message = "A senha não pode estar em branco.")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
                message = "A senha deve conter no mínimo 8 caracteres, incluindo pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial.")
        String novaSenha,

        @Schema(description = "Código de verificação de segurança", example = "123456")
        @NotBlank(message = "O código de verificação é obrigatório.")
        @Size(min = 6, max = 6, message = "O código deve ter exatamente 6 dígitos.")
        String codigoVerificado

) {}
