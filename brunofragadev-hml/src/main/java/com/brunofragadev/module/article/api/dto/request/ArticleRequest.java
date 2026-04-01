package com.brunofragadev.module.article.api.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Set;

@Schema(description = "Objeto de requisição para criação e atualização de artigos")
public record ArticleRequest(

        @Schema(description = "Título principal do artigo", example = "Microsserviços com Spring Boot")
        @NotBlank(message = "O título não pode estar em branco")
        @Size(min = 5, max = 150, message = "O título deve ter entre 5 e 150 caracteres")
        String title,

        @Schema(description = "Subtítulo ou chamada curta do artigo", example = "Aprenda a escalar sua arquitetura de forma eficiente.")
        @Size(max = 255, message = "O subtítulo não pode exceder 255 caracteres")
        String subtitle,

        @Schema(description = "Identificador amigável da URL (Slug)", example = "microsservicos-com-spring-boot")
        @NotBlank(message = "O slug é obrigatório")
        @Pattern(regexp = "^[a-z0-9-]+$", message = "O slug deve conter apenas letras minúsculas, números e hifens")
        String slug,

        @Schema(description = "Lista de etiquetas para categorização", example = "[\"Java\", \"Backend\"]")
        @NotEmpty(message = "Adicione pelo menos uma tag ao artigo")
        Set<@Size(max = 30) String> tags,

        @Schema(description = "URL da imagem de capa (Cloudinary)", example = "https://res.cloudinary.com/...)")
        @NotBlank(message = "A imagem de capa é obrigatória")
        String coverImage,

        @Schema(description = "Nome da fonte escolhida para o artigo", example = "Instrument Serif")
        @NotBlank(message = "A família da fonte deve ser informada")
        String fontFamily,

        @Schema(description = "Conteúdo do artigo em formato HTML formatado")
        @NotBlank(message = "O conteúdo HTML não pode ser vazio")
        String contentHtml,

        @Schema(description = "Status do artigo (RASCUNHO ou PUBLICADO)", example = "PUBLICADO")
        @Pattern(regexp = "^(?i)(RASCUNHO|PUBLICADO)$", message = "Status inválido. Use RASCUNHO ou PUBLICADO")
        String status,

        @Schema(description = "Estrutura JSON original do editor TipTap",
                example = "{\"type\": \"doc\", \"content\": [...]}")
        @NotNull(message = "O JSON estrutural é obrigatório para futuras edições")
        JsonNode contentJson

) {
    /**
     * Método auxiliar (Rico) para converter o JsonNode em String
     * antes de passar para a Entity.
     */
    public String contentJsonToString() {
        return this.contentJson != null ? this.contentJson.toString() : null;
    }
}