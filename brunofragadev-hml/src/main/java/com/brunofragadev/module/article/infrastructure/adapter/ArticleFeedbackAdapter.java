package com.brunofragadev.module.article.infrastructure.adapter;

import com.brunofragadev.module.article.application.usecase.GetArticleByIdUseCase;
import com.brunofragadev.module.article.domain.entity.Article;
import com.brunofragadev.module.feedback.domain.port.FeedbackArticlePort;
import org.springframework.stereotype.Component;

@Component
public class ArticleFeedbackAdapter implements FeedbackArticlePort {

    private final GetArticleByIdUseCase getArticleByIdUseCase;

    public ArticleFeedbackAdapter(GetArticleByIdUseCase getArticleByIdUseCase) {
        this.getArticleByIdUseCase = getArticleByIdUseCase;
    }

    @Override
    public String resolveName(Long id){
        Article article = getArticleByIdUseCase.execute(id);
        return article.getTitle();
    }
}
