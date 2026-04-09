package com.brunofragadev.module.feedback.application.listener;

import com.brunofragadev.infrastructure.email.EmailService;
import com.brunofragadev.module.feedback.domain.event.NewFeedbackEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SendEmailOnNewFeedbackListener {

    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;

    public SendEmailOnNewFeedbackListener(EmailService emailService, ApplicationEventPublisher eventPublisher) {
        this.emailService = emailService;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreateNewFeedback(NewFeedbackEvent event){
            emailService.sendNewFeedbackAlertToAdmin(event.feedbackDTO(), event.feedbackLocation());
    }
}

