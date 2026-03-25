package com.brunofragadev.module.feedback.application.usecase;

import com.brunofragadev.module.feedback.api.dto.request.FeedbackCreateRequest;
import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.feedback.domain.entity.Feedback;
import com.brunofragadev.module.feedback.domain.exception.FeedbackNotFoundException;
import com.brunofragadev.module.feedback.domain.repository.FeedbackRepository;
import com.brunofragadev.module.feedback.infrastructure.mapper.FeedbackMapper;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.module.user.domain.exception.InvalidCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class UpdateFeedbackUseCase {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    public UpdateFeedbackUseCase(FeedbackRepository feedbackRepository, FeedbackMapper feedbackMapper) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
    }

    public FeedbackDTO execute(Long feedbackId, FeedbackCreateRequest request, User user) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new FeedbackNotFoundException("Feedback não encontrado!"));
        if (!feedback.getUser().getId().equals(user.getId())) {
            throw new InvalidCredentialsException("Acesso negado: Você não tem permissão para editar este feedback.");
        }
        feedback.setDescricao(request.descricao());
        feedback.setAvaliacao(request.avaliacao());
        feedbackRepository.save(feedback);
        return feedbackMapper.toDTO(feedback);
    }
}