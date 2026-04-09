package com.brunofragadev.module.user.application.listener;

import com.brunofragadev.infrastructure.email.EmailService;
import com.brunofragadev.module.user.domain.event.ActivateAccountEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SendWelcomeEmailListener {

    private EmailService emailService;

    public SendWelcomeEmailListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onVerificationSuccess(ActivateAccountEvent event){
        emailService.sendWelcomeEmail(event.userDTO().email(), event.userDTO().nome());
    }
}
