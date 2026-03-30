package com.brunofragadev.module.article.infrastructure;

import com.brunofragadev.module.article.domain.Article;
import com.brunofragadev.module.article.domain.ArticleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataArticleRepository extends JpaRepository<Article, Long> {
    boolean existsBySlug(String slug);
    java.util.Optional<Article> findBySlug(String slug);
    List<Article> findAllByStatus(ArticleStatus status);
    List<Article> findAll();
}