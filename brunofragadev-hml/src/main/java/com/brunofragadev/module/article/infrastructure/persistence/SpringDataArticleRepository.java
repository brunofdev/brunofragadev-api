package com.brunofragadev.module.article.infrastructure.persistence;

import com.brunofragadev.module.article.api.dto.response.ArticleSummaryResponse;
import com.brunofragadev.module.article.domain.entity.Article;
import com.brunofragadev.module.article.domain.entity.ArticleStatus;

// 👈 O CORRETO É ESTE (JPA):
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SpringDataArticleRepository extends JpaRepository<Article, Long> {
    boolean existsBySlug(String slug);
    Optional<Article> findBySlug(String slug);
    List<Article> findAllByStatus(ArticleStatus status);
    List<Article> findTop5ByStatusOrderByDataCriacaoDesc(ArticleStatus status);
}