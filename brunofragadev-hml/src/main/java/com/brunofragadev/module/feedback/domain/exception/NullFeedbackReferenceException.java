package com.brunofragadev.module.feedback.domain.exception;

import com.brunofragadev.shared.domain.exception.BusinessException;

public class NullFeedbackReferenceException extends BusinessException {
    public NullFeedbackReferenceException(String message){
        super(message);
    }
}
