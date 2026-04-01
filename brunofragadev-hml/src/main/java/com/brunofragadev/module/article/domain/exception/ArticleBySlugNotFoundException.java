package com.brunofragadev.module.article.domain.exception;

import com.brunofragadev.shared.domain.BusinessException;

public class ArticleBySlugNotFoundException extends BusinessException {
    public ArticleBySlugNotFoundException(String message) {
        super(message);
    }
}