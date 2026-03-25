package com.brunofragadev.module.user.domain.exception;

public class UserEmailNotRegisteredException extends RuntimeException{
    public UserEmailNotRegisteredException(String message){
        super(message);
    }
}
