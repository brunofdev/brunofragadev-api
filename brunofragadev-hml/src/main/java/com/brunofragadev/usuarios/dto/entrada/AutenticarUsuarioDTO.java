package com.brunofragadev.usuarios.dto.entrada;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para autenticação de usuário")
public record AutenticarUsuarioDTO(
        @Schema(description = "Nome de usuário para login", example = "brunodev")
        @NotBlank(message = "O nome de usuário não pode estar em branco.")
        @Size(min = 5, max = 20, message = "O nome de usuário deve ter entre 5 e 20 caracteres.")
        @Pattern(regexp = "\\S+", message = "O nome de usuário não pode conter espaços em branco.")
        String userName,

        @Schema(description = "Código de autenticação", example = "123456")
        @NotBlank(message = "O código não pode estar em branco.")
        String codigo
){
}