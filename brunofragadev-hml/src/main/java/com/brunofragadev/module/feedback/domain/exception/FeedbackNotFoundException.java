package com.brunofragadev.module.feedback.domain.exception;

public class FeedbackNotFoundException extends RuntimeException{
    public FeedbackNotFoundException(String message){
        super(message);
    }
}
