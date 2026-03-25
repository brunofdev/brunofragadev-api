package com.brunofragadev.module.feedback.application.usecase;

import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.feedback.domain.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListProjectFeedbacksUseCase {

    private final FeedbackRepository feedbackRepository;

    public ListProjectFeedbacksUseCase(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<FeedbackDTO> execute(Long projectId) {
        return feedbackRepository.findProjectFeedbacksWithPhotos(projectId);
    }
}