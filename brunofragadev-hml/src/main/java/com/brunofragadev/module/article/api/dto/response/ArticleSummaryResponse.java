package com.brunofragadev.module.article.api.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public record ArticleSummaryResponse(
        Long id,
        String title,
        String subtitle,
        String slug,
        String coverImage,
        Set<String> tags,
        String criadoPor,
        LocalDateTime dataCriacao
) {}