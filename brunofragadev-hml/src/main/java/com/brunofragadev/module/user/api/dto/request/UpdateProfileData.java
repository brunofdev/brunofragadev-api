package com.brunofragadev.module.user.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

@Schema(description = "DTO para atualização de dados do perfil do usuário")
public record UpdateProfileData(
        @Schema(description = "Nome público do usuário", example = "Bruno Dev", nullable = true)
        String nomePublico,

        @Schema(description = "Indica se o perfil é anônimo", example = "false", nullable = true)
        Boolean isAnonimo,

        @Schema(description = "Profissão do usuário", example = "Desenvolvedor de Software", nullable = true)
        @Size(max = 100, message = "A profissão deve ter no máximo 100 caracteres.")
        String profissao,

        // SOMENTO NUMEROS BRS POR ENQUANTO
        @Schema(description = "Telefone do usuário (com ou sem DDI)", example = "+5548999999999", nullable = true)
        @Pattern(regexp = "^(\\+55)?[1-9]{2}9\\d{8}$",message = "Informe um celular brasileiro válido com DDD.")
        String telefone,

        @Schema(description = "País do usuário", example = "Brasil", nullable = true)
        @Size(max = 50, message = "O país deve ter no máximo 50 caracteres.")
        String pais,

        @Schema(description = "Cidade do usuário", example = "Porto Alegre", nullable = true)
        @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres.")
        String cidade,

        // Garante que o link seja do GitHub
        @Schema(description = "Link para o perfil no GitHub", example = "https://github.com/brunodev", nullable = true)
        @Pattern(regexp = "^(https?://)?(www\\.)?github\\.com/.*", message = "Deve ser uma URL válida do GitHub.")
        String gitHub,

        // Garante que o link seja do LinkedIn
        @Schema(description = "Link para o perfil no LinkedIn", example = "https://linkedin.com/in/brunodev", nullable = true)
        @Pattern(regexp = "^(https?://)?(www\\.)?linkedin\\.com/.*", message = "Deve ser uma URL válida do LinkedIn.")
        String linkedin,

        // A sua regra de ouro para a Bio
        @Schema(description = "Biografia do usuário", example = "Desenvolvedor apaixonado por tecnologia.", nullable = true)
        @Size(max = 1000, message = "A bio deve ter no máximo 1000 caracteres.")
        String bio,

        // Se a foto for trafegada como Link, validamos se é uma URL real
        @Schema(description = "URL da foto de perfil", example = "https://example.com/foto.jpg", nullable = true)
        @URL(message = "A foto de perfil deve ser um link (URL) válido.")
        String fotoPerfil
) {
}