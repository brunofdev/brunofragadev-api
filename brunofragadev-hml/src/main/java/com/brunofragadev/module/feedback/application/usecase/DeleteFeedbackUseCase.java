package com.brunofragadev.module.feedback.application.usecase;

import com.brunofragadev.module.feedback.domain.entity.Feedback;
import com.brunofragadev.module.feedback.domain.exception.FeedbackNotFoundException;
import com.brunofragadev.module.feedback.domain.repository.FeedbackRepository;
import com.brunofragadev.module.user.domain.entity.Role;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.module.user.domain.exception.InvalidCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class DeleteFeedbackUseCase {

    private final FeedbackRepository feedbackRepository;

    public DeleteFeedbackUseCase(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public void execute(Long feedbackId, User user) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new FeedbackNotFoundException("Feedback não encontrado!"));

        boolean isCommentOwner = feedback.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Role.ADMIN3;

        if (!isCommentOwner && !isAdmin) {
            throw new InvalidCredentialsException("Acesso negado: Você não tem permissão para excluir este feedback.");
        }

        feedbackRepository.delete(feedback);
    }
}