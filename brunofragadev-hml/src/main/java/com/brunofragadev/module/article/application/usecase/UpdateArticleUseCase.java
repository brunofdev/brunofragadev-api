package com.brunofragadev.module.article.application.usecase;

import com.brunofragadev.module.article.domain.entity.Article;
import com.brunofragadev.module.article.domain.entity.ArticleStatus;
import com.brunofragadev.module.article.domain.exception.ArticleNotFoundException;
import com.brunofragadev.module.article.domain.exception.SlugAlreadyInUseException;
import com.brunofragadev.module.article.domain.repository.ArticleRepository;
import com.brunofragadev.module.article.api.dto.request.ArticleRequest;
import com.brunofragadev.module.article.api.dto.response.ArticleResponse;
import com.brunofragadev.module.article.application.mapper.ArticleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateArticleUseCase {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Transactional
    public ArticleResponse execute(Long id, ArticleRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Artigo não encontrado com o ID: " + id));

        if (!article.getSlug().equals(request.slug()) && articleRepository.existsBySlug(request.slug())) {
            throw new SlugAlreadyInUseException("O slug '" + request.slug() + "' já está em uso.");
        }

        article.atualizar(
                request.title(),
                request.subtitle(),
                request.slug(),
                request.coverImage(),
                request.fontFamily(),
                request.contentHtml(),
                request.contentJsonToString(),
                request.tags(),
                request.status() != null ? ArticleStatus.valueOf(request.status().toUpperCase()) : article.getStatus()
        );

        articleRepository.save(article);
        return articleMapper.toResponse(article);
    }
}