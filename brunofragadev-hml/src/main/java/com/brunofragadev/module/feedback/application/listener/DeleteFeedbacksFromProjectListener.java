package com.brunofragadev.module.feedback.application.listener;

import com.brunofragadev.module.feedback.application.usecase.RemoveAllProjectFeedbacksUseCase;
import com.brunofragadev.module.project.domain.event.ProjectDeletedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DeleteFeedbacksFromProjectListener {

    private final RemoveAllProjectFeedbacksUseCase removeAllProjectFeedbacksUseCase;


    public DeleteFeedbacksFromProjectListener(RemoveAllProjectFeedbacksUseCase removeAllProjectFeedbacksUseCase) {
        this.removeAllProjectFeedbacksUseCase = removeAllProjectFeedbacksUseCase;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDeleteProject (ProjectDeletedEvent event){
        removeAllProjectFeedbacksUseCase.execute(event.referenceId());
    }
}
