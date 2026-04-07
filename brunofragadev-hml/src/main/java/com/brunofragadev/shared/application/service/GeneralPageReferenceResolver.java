package com.brunofragadev.shared.application.service;

import com.brunofragadev.module.feedback.domain.entity.FeedbackType;
import com.brunofragadev.shared.domain.repository.ReferenceResolverInterface;
import org.springframework.stereotype.Component;

/**
 * Resolve referências do tipo {@link FeedbackType#GERAL}.
 * Referencia de id sempre deve ser 0 (zero) para feedbacks
 * criados na pagina geral.
 */
@Component
public class GeneralPageReferenceResolver implements ReferenceResolverInterface {

    @Override
    public FeedbackType getType(){
        return FeedbackType.GERAL;
    }

    @Override
    public String resolveName(Long referenceId) {
            return "Página geral";
    }
}