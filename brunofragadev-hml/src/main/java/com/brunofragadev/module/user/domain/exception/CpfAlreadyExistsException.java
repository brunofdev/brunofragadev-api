package com.brunofragadev.module.user.domain.exception;

public class CpfAlreadyExistsException extends RuntimeException{
    public CpfAlreadyExistsException(String message){
        super(message);
    }
}
