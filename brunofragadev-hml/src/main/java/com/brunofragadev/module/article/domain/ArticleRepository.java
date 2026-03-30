package com.brunofragadev.module.article.domain;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    Article save(Article article);
    boolean existsBySlug(String slug);
    Optional<Article> findBySlug(String slug);
    List<Article> findAllByStatus(ArticleStatus status);
    List<Article> findAll();
}