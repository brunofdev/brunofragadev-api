package com.brunofragadev.module.feedback.application.usecase;

import com.brunofragadev.infrastructure.email.EmailService;
import com.brunofragadev.module.feedback.api.dto.request.FeedbackCreateRequest;
import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.feedback.domain.entity.Feedback;
import com.brunofragadev.module.feedback.domain.repository.FeedbackRepository;
import com.brunofragadev.module.feedback.infrastructure.mapper.FeedbackMapper;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.shared.service.ReferenceResolver;
import org.springframework.stereotype.Service;

@Service
public class CreateFeedbackUseCase {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final EmailService emailService;
    private final ReferenceResolver referenceResolver;

    public CreateFeedbackUseCase(FeedbackRepository feedbackRepository,
                                 FeedbackMapper feedbackMapper,
                                 EmailService emailService,
                                 ReferenceResolver referenceResolver) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
        this.emailService = emailService;
        this.referenceResolver = referenceResolver;
    }

    public FeedbackDTO execute(FeedbackCreateRequest request, User user) {
        Feedback feedback = feedbackMapper.toNewFeedback(request, user);
        feedbackRepository.save(feedback);
        FeedbackDTO feedbackDTO = feedbackMapper.toDTO(feedback);
        String feedbackLocation = referenceResolver.resolveName(feedbackDTO.feedbackType(), feedbackDTO.referenciaId());
        emailService.sendNewFeedbackAlertToAdmin(feedbackDTO, feedbackLocation);
        return feedbackDTO;
    }
}