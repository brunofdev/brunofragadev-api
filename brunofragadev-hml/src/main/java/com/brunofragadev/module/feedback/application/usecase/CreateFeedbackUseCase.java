package com.brunofragadev.module.feedback.application.usecase;

import com.brunofragadev.infrastructure.email.EmailService;
import com.brunofragadev.module.feedback.api.dto.request.FeedbackCreateRequest;
import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.feedback.domain.entity.Feedback;
import com.brunofragadev.module.feedback.domain.event.NewFeedbackEvent;
import com.brunofragadev.module.feedback.domain.repository.FeedbackRepository;
import com.brunofragadev.module.feedback.application.mapper.FeedbackMapper;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.shared.application.service.ReferenceResolverFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateFeedbackUseCase {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    private final ReferenceResolverFactory resolverFactory;
    private final ApplicationEventPublisher eventPublisher;

    public CreateFeedbackUseCase(FeedbackRepository feedbackRepository,
                                 FeedbackMapper feedbackMapper,
                                 ReferenceResolverFactory resolverFactory,
                                 ApplicationEventPublisher eventPublisher) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
        this.resolverFactory = resolverFactory;
        this.eventPublisher = eventPublisher;
    }
    @Transactional
    public FeedbackDTO execute(FeedbackCreateRequest request, User user) {
        Feedback feedback = feedbackMapper.toNewFeedback(request, user);
        feedbackRepository.save(feedback);
        FeedbackDTO feedbackDTO = feedbackMapper.toDTO(feedback);
        String feedbackLocation = resolverFactory.resolveName(feedbackDTO.feedbackType(), feedbackDTO.referenciaId());
        eventPublisher.publishEvent(new NewFeedbackEvent(feedbackDTO, feedbackLocation));
        return feedbackDTO;
    }
}