package com.brunofragadev.module.article.application;

import com.brunofragadev.module.article.api.ArticleRequest;
import com.brunofragadev.module.article.api.ArticleResponse;
import com.brunofragadev.module.article.domain.Article;
import com.brunofragadev.module.article.domain.ArticleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateArticleUseCase {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    public CreateArticleUseCase(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    @Transactional
    public ArticleResponse execute(ArticleRequest request) {

        validateSlugUniqueness(request.slug());
        Article article = articleMapper.toEntity(request);
        articleRepository.save(article);
        return articleMapper.toResponse(article);
    }

    private void validateSlugUniqueness(String slug) {
        if (articleRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("O slug '" + slug + "' já está em uso por outro artigo.");
        }
    }
}