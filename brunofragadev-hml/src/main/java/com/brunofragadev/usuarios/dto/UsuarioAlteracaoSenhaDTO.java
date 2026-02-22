package com.brunofragadev.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record UsuarioAlteracaoSenhaDTO(


        String userName,

        @Schema(description = "Senha forte (min 8 chars, 1 maiúscula, 1 minúscula, 1 número, 1 especial)", example = "NovaSenha@123")
        @NotBlank(message = "A nova senha não pode estar em branco.")
        @Size(min = 8, message = "A nova senha deve ter no mínimo 8 caracteres.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "A nova senha deve conter no mínimo 8 caracteres, letras maiúsculas, minúsculas, números e caracteres especiais.")
        String novaSenha,

        @Schema(description = "Código de verificação de segurança", example = "123456")
        @NotBlank(message = "O código de verificação é obrigatório.")
        @Size(min = 6, max = 6, message = "O código deve ter exatamente 6 dígitos.")
        String codigoVerificado

) {
    // Método para criar uma cópia do Record substituindo a senha plana pela criptografada
    // e já padronizando o userName para maiúsculo
    public UsuarioAlteracaoSenhaDTO withNovaSenha(String senhaCriptografada) {
        return new UsuarioAlteracaoSenhaDTO(
                this.userName().toUpperCase(),
                senhaCriptografada,
                this.codigoVerificado()
        );
    }

    // Impede que a senha seja printada no console (Logs)
    @Override
    public String toString() {
        return "UsuarioAlteracaoSenhaDTO[" +
                "userName='" + userName + '\'' +
                ", codigoVerificado='" + codigoVerificado + '\'' +
                ", novaSenha='*****'" +
                ']';
    }
}