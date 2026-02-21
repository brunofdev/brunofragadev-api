package com.brunofragadev.usuarios.exceptions;

public class VerificationCodeInvalidException extends RuntimeException{
    public VerificationCodeInvalidException (String message){
        super(message);
    }
}
