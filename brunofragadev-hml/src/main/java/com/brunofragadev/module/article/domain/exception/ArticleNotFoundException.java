package com.brunofragadev.module.article.domain.exception;

import com.brunofragadev.shared.domain.exception.BusinessException;

public class ArticleNotFoundException extends BusinessException {
    public ArticleNotFoundException(String message) {
        super(message);
    }
}