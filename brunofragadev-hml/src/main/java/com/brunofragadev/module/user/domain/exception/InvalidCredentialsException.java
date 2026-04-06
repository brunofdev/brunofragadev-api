package com.brunofragadev.module.user.domain.exception;

import com.brunofragadev.shared.domain.exception.BusinessException;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException (String message){
        super(message);
    }
}
