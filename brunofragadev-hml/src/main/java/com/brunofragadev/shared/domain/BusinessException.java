package com.brunofragadev.shared.domain;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}