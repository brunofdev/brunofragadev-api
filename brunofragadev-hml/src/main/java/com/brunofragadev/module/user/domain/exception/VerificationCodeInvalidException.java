package com.brunofragadev.module.user.domain.exception;

import com.brunofragadev.shared.domain.exception.BusinessException;

public class VerificationCodeInvalidException extends BusinessException {
    public VerificationCodeInvalidException (String message){
        super(message);
    }
}
