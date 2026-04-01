package com.brunofragadev.module.article.application.usecase;

import com.brunofragadev.module.article.api.dto.request.ArticleRequest;
import com.brunofragadev.module.article.api.dto.response.ArticleResponse;
import com.brunofragadev.module.article.application.mapper.ArticleMapper;
import com.brunofragadev.module.article.domain.entity.Article;
import com.brunofragadev.module.article.domain.exception.SlugAlreadyInUseException;
import com.brunofragadev.module.article.domain.repository.ArticleRepository;
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
            throw new SlugAlreadyInUseException("O slug '" + slug + "' já está em uso por outro artigo.");
        }
    }
}