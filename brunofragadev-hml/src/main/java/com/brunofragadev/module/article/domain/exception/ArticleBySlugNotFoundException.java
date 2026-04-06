package com.brunofragadev.module.article.domain.exception;

import com.brunofragadev.shared.domain.exception.BusinessException;

public class ArticleBySlugNotFoundException extends BusinessException {
    public ArticleBySlugNotFoundException(String message) {
        super(message);
    }
}