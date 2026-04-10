package com.brunofragadev.module.feedback.application.usecase;


import com.brunofragadev.module.feedback.domain.entity.Feedback;
import com.brunofragadev.module.feedback.domain.exception.FeedbackNotFoundException;
import com.brunofragadev.module.feedback.domain.exception.NullFeedbackReferenceException;
import com.brunofragadev.module.feedback.domain.repository.FeedbackRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de Uso responsável por remover todos os feedbacks associados a um projeto específico.
 * <p>
 * Esta classe atua como uma rotina de limpeza de dados (Cascade Delete lógico) para manter a
 * integridade entre os módulos (Bounded Contexts). Ela é projetada para ser acionada no
 * momento em que um projeto é removido do sistema, garantindo que nenhum feedback
 * permaneça como "órfão" no banco de dados, ocupando espaço desnecessário.
 * </p>
 *
 * @author Bruno de Fraga
 * @version 1.0
 * @since 2026-03
 */

@Component
public class RemoveAllProjectFeedbacksUseCase {

    private final FeedbackRepository feedbackRepository;

    public RemoveAllProjectFeedbacksUseCase(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public void execute (Long referenceId){
        if (referenceId == null){
            throw new NullFeedbackReferenceException("Referencia não pode ser vazia para deletar os feedbacks relacionados a um projeto");
        }
        feedbackRepository.deleteAllByReferenceId(referenceId);
    }

}
