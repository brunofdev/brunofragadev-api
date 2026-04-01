package com.brunofragadev.module.article.api.dto.response;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.Set;

public record ArticleResponse(
        Long id,
        String title,
        String subtitle,
        String slug,
        String coverImage,
        String status,
        String fontFamily,
        String contentHtml,
        JsonNode contentJson, // Retornamos como JSON real para o Front ler fácil
        Set<String> tags,
        String criadoPor,
        LocalDateTime dataCriacao
) {}