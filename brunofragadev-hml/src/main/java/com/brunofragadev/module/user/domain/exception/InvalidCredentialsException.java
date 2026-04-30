package com.brunofragadev.module.user.domain.exception;

import com.brunofragadev.shared.domain.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException (String message){
        super(message);
    }
}
