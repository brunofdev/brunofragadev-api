package com.brunofragadev.module.article.application;

import com.brunofragadev.module.article.api.ArticleResponse;
import com.brunofragadev.module.article.domain.ArticleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class GetArticleBySlugUseCase {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    public GetArticleBySlugUseCase(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    @Transactional(readOnly = true)
    public ArticleResponse execute(String slug) {
        return articleRepository.findBySlug(slug)
                .map(articleMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Artigo não encontrado com o slug: " + slug));
    }
}