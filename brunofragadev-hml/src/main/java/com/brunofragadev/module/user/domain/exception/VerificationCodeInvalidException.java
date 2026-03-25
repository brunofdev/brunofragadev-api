package com.brunofragadev.module.user.domain.exception;

public class VerificationCodeInvalidException extends RuntimeException{
    public VerificationCodeInvalidException (String message){
        super(message);
    }
}
