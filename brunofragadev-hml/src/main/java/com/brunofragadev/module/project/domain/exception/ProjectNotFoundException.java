package com.brunofragadev.module.project.domain.exception;

import com.brunofragadev.shared.domain.exception.BusinessException;

public class ProjectNotFoundException extends BusinessException {
    public ProjectNotFoundException(String message){
        super(message);
    }
}
