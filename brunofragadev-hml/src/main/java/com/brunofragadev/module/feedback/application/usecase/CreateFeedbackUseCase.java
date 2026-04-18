package com.brunofragadev.module.feedback.application.usecase;

import com.brunofragadev.module.feedback.api.dto.request.FeedbackCreateRequest;
import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.feedback.domain.entity.Feedback;
import com.brunofragadev.module.feedback.domain.entity.FeedbackType;
import com.brunofragadev.module.feedback.domain.event.NewFeedbackEvent;
import com.brunofragadev.module.feedback.domain.port.FeedbackArticlePort;
import com.brunofragadev.module.feedback.domain.port.FeedbackProjectPort;
import com.brunofragadev.module.feedback.domain.repository.FeedbackRepository;
import com.brunofragadev.module.feedback.application.mapper.FeedbackMapper;
import com.brunofragadev.module.user.domain.entity.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateFeedbackUseCase {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final FeedbackArticlePort feedbackArticlePort;
    private final FeedbackProjectPort feedbackProjectPort;

    public CreateFeedbackUseCase(FeedbackRepository feedbackRepository,
                                 FeedbackMapper feedbackMapper,
                                 ApplicationEventPublisher eventPublisher,
                                 FeedbackArticlePort feedbackArticlePort,
                                 FeedbackProjectPort feedbackProjectPort) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
        this.eventPublisher = eventPublisher;
        this.feedbackArticlePort = feedbackArticlePort;
        this.feedbackProjectPort = feedbackProjectPort;
    }
    @Transactional
    public FeedbackDTO execute(FeedbackCreateRequest request, User user) {
        Feedback feedback = feedbackMapper.toNewFeedback(request, user);
        feedbackRepository.save(feedback);
        FeedbackDTO feedbackDTO = feedbackMapper.toDTO(feedback);
        String feedbackTitle = resolveLocation(feedbackDTO.feedbackType(), feedbackDTO.referenciaId());
        eventPublisher.publishEvent(new NewFeedbackEvent(feedbackDTO, feedbackTitle));
        return feedbackDTO;
    }
    private String resolveLocation (FeedbackType feedbackType, Long id){
        String title;
        switch (feedbackType){
            case ARTIGO -> title = "Artigo: " + feedbackArticlePort.resolveName(id);
            case PROJETO -> title = "Projeto: " + feedbackProjectPort.resolveName(id);
            default -> title = "Pagina Home";

        }
        return title;
    }
}