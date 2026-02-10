package com.brunofragadev.usuarios.exceptions;

public class UserDontFoundException extends RuntimeException{
    public UserDontFoundException (String message){
        super(message);
    }
}
