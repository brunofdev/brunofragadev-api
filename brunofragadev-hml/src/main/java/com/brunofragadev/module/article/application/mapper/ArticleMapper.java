package com.brunofragadev.module.article.application.mapper;

import com.brunofragadev.module.article.api.dto.request.ArticleRequest;
import com.brunofragadev.module.article.api.dto.response.ArticleResponse;
import com.brunofragadev.module.article.api.dto.response.ArticleSummaryResponse;
import com.brunofragadev.module.article.domain.entity.Article;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public ArticleSummaryResponse toSummaryResponse(Article article) {
        return new ArticleSummaryResponse(
                article.getId(),
                article.getTitle(),
                article.getSubtitle(),
                article.getSlug(),
                article.getCoverImage(),
                article.getTags(), // O Set<String> entra aqui sem erro
                article.getCriadoPor(),
                article.getDataCriacao()
        );
    }
    public List<ArticleSummaryResponse> toSummaryResponseList(List<Article> articles) {
        if (articles == null) {
            return java.util.Collections.emptyList();
        }
        return articles.stream()
                .map(this::toSummaryResponse)
                .toList();
    }
}