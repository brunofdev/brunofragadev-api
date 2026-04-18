package com.brunofragadev.module.article.application.usecase;

import com.brunofragadev.module.article.domain.entity.Article;
import com.brunofragadev.module.article.domain.exception.ArticleNotFoundException;
import com.brunofragadev.module.article.domain.repository.ArticleRepository;
import org.springframework.stereotype.Component;

@Component
public class GetArticleByIdUseCase {

    private final ArticleRepository repository;

    public GetArticleByIdUseCase(ArticleRepository repository) {
        this.repository = repository;
    }

    public Article execute(Long id){
        return repository.findById(id).orElseThrow(() -> new ArticleNotFoundException("Artigo com o id: " + id + " não localizado"));
    }


}
