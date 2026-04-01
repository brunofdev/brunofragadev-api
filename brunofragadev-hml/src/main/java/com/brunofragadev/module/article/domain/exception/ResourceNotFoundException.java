package com.brunofragadev.module.article.domain.exception;

import com.brunofragadev.shared.domain.BusinessException;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(String.format("%s não encontrado com o identificador: %s", resourceName, identifier));
    }
}