package com.brunofragadev.module.user.domain.exception;

import com.brunofragadev.shared.domain.exception.BusinessException;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message){
        super(message);
    }
}
