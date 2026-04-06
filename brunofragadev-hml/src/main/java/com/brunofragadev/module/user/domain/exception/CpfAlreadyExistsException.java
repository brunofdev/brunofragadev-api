package com.brunofragadev.module.user.domain.exception;

import com.brunofragadev.shared.domain.exception.BusinessException;

public class CpfAlreadyExistsException extends BusinessException {
    public CpfAlreadyExistsException(String message){
        super(message);
    }
}
