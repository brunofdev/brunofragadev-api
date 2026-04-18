package com.brunofragadev.module.article.infrastructure.adapter;


import com.brunofragadev.module.article.domain.entity.Article;
import com.brunofragadev.module.article.domain.repository.ArticleRepository;
import com.brunofragadev.module.article.domain.entity.ArticleStatus;
import com.brunofragadev.module.article.infrastructure.persistence.SpringDataArticleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ArticleRepositoryAdapter implements ArticleRepository {

    private final SpringDataArticleRepository repository;

    public ArticleRepositoryAdapter(SpringDataArticleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Article save(Article article) { return repository.save(article); }

    @Override
    public boolean existsBySlug(String slug) { return repository.existsBySlug(slug); }

    @Override
    public Optional<Article> findBySlug(String slug) { return repository.findBySlug(slug); }

    @Override
    public List<Article> findAllByStatus(ArticleStatus status) {
        return repository.findAllByStatus(status);
    }
    @Override
    public List<Article> findAll() {
        return repository.findAll();
    }
    @Override
    public Optional<Article> findById(Long id) {
        return repository.findById(id);
    }
    @Override
    public List<Article> findLatestPublished() {
        return repository.findTop5ByStatusOrderByDataCriacaoDesc(ArticleStatus.PUBLICADO);
    }
}