package com.brunofragadev.shared.application.service;

import com.brunofragadev.module.article.domain.entity.Article;
import com.brunofragadev.module.article.domain.repository.ArticleRepository;
import com.brunofragadev.module.feedback.domain.entity.FeedbackType;
import com.brunofragadev.shared.domain.repository.ReferenceResolverInterface;
import org.springframework.stereotype.Component;


/**
 * Resolve referências do tipo {@link FeedbackType#ARTIGO}.
 * Busca o título do artigo pelo ID usando {@link ArticleRepository}.
 */
@Component
public class ArticleReferenceResolver implements ReferenceResolverInterface {

    private final ArticleRepository articleRepository;

    public ArticleReferenceResolver(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public FeedbackType getType (){
        return FeedbackType.ARTIGO;
    }

    @Override
    public String resolveName(Long referenceId) {
            return "Postado no Artigo: " + articleRepository.findById(referenceId)
                    .map(Article::getTitle)
                    .orElse("Artigo não localizado");
    }
}
