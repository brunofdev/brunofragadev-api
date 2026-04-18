package com.brunofragadev.module.feedback.application.validator;

import com.brunofragadev.module.feedback.domain.entity.FeedbackType;
import com.brunofragadev.module.project.application.usecase.GetProjectByIdUseCase;
import org.springframework.stereotype.Component;

@Component
public class feedbackCreateValidator {

    private final GetProjectByIdUseCase getProjectByIdUseCase;

    public feedbackCreateValidator(GetProjectByIdUseCase getProjectByIdUseCase) {
        this.getProjectByIdUseCase = getProjectByIdUseCase;
    }

    public void validarReferencia(FeedbackType tipo, Long referenciaId) {
        if (tipo == FeedbackType.PROJETO) {
            if (referenciaId == null) {
                throw new IllegalArgumentException("O ID de referência é obrigatório para feedbacks de projeto.");
            }
            getProjectByIdUseCase.returnDTO(referenciaId);
        }
    }
}
