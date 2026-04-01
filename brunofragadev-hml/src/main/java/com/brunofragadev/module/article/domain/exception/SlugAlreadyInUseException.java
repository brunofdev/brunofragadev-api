package com.brunofragadev.module.article.domain.exception;

import com.brunofragadev.shared.domain.BusinessException;

public class SlugAlreadyInUseException extends BusinessException {
    public SlugAlreadyInUseException(String message) {
        super(message);
    }
}