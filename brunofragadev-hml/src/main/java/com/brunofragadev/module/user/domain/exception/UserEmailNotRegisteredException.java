package com.brunofragadev.module.user.domain.exception;

import com.brunofragadev.shared.domain.exception.BusinessException;

public class UserEmailNotRegisteredException extends BusinessException {
    public UserEmailNotRegisteredException(String message){
        super(message);
    }
}
