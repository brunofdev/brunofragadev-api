package com.brunofragadev.module.article.application.usecase;

import com.brunofragadev.module.article.api.dto.response.ArticleSummaryResponse;
import com.brunofragadev.module.article.application.mapper.ArticleMapper;
import com.brunofragadev.module.article.domain.repository.ArticleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ListLatestArticlesUseCase {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    public ListLatestArticlesUseCase(ArticleRepository articleRepository,
                                     ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    public List<ArticleSummaryResponse> execute() {
        return articleMapper.toSummaryResponseList(articleRepository.findLatestPublished());
    }
}