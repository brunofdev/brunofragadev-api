package com.brunofragadev.module.article.domain.exception;

import com.brunofragadev.shared.domain.exception.BusinessException;

public class InvalidArticleStatusException extends BusinessException {
    public InvalidArticleStatusException(String status) {
        super("O status '" + status + "' é inválido. Utilize apenas RASCUNHO ou PUBLICADO.");
    }
}