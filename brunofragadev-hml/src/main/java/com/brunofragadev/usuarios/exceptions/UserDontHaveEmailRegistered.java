package com.brunofragadev.usuarios.exceptions;

public class UserDontHaveEmailRegistered extends RuntimeException{
    public UserDontHaveEmailRegistered(String message){
        super(message);
    }
}
