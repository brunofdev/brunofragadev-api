package com.brunofragadev.module.feedback.application.usecase;

import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.feedback.domain.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListArticleFeedbacksUseCase {

    private final FeedbackRepository feedbackRepository;

    public ListArticleFeedbacksUseCase(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<FeedbackDTO> execute(Long idArtigo) {
        // O repositório já devolve a lista de DTOs montadinha e ordenada!
        return feedbackRepository.findArticleFeedbacksWithPhotos(idArtigo);
    }
}