package com.brunofragadev.module.feedback.domain.event;

import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;

public record NewFeedbackEvent(
        FeedbackDTO feedbackDTO,
        String feedbackLocation) {
}
