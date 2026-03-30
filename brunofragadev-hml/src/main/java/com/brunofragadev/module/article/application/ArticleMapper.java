package com.brunofragadev.module.article.application;

import com.brunofragadev.module.article.api.ArticleRequest;
import com.brunofragadev.module.article.api.ArticleResponse;
import com.brunofragadev.module.article.domain.Article;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    private final ObjectMapper objectMapper;

    public ArticleMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Article toEntity(ArticleRequest request) {
        return Article.criar(
                request.title(),
                request.subtitle(),
                request.slug(),
                request.coverImage(),
                request.fontFamily(),
                request.contentHtml(),
                request.contentJson().toString(),
                request.tags()
        );
    }

    public ArticleResponse toResponse(Article entity) {
        try {
            JsonNode jsonNode = objectMapper.readTree(entity.getContentJson());

            return new ArticleResponse(
                    entity.getId(),
                    entity.getTitle(),
                    entity.getSubtitle(),
                    entity.getSlug(),
                    entity.getCoverImage(),
                    entity.getStatus().name(),
                    entity.getFontFamily(),
                    entity.getContentHtml(),
                    jsonNode,
                    entity.getTags(),
                    entity.getCriadoPor(), // Vem do Auditable
                    entity.getDataCriacao() // Vem do Auditable
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter JSON do artigo", e);
        }
    }
}