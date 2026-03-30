package com.brunofragadev.module.article.application;

import com.brunofragadev.module.article.api.ArticleResponse;
import com.brunofragadev.module.article.domain.ArticleRepository;
import com.brunofragadev.module.article.domain.ArticleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListPublishedArticlesUseCase {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    public List<ArticleResponse> execute() {
        return articleRepository.findAll()
                .stream()
                .map(articleMapper::toResponse)
                .collect(Collectors.toList());
    }
}