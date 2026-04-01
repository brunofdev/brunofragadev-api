package com.brunofragadev.module.feedback.domain.repository;

import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.feedback.domain.entity.Feedback;
import com.brunofragadev.module.feedback.domain.entity.FeedbackType;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository {
    Feedback save(Feedback feedback);
    Optional<Feedback> findById(Long id);
    void delete(Feedback feedback);

    List<Feedback> findAllFeedbackByProjectReferenceId(Long referenceId);
    void  deleteAllByReferenceId(Long referenceId) ;
    List<FeedbackDTO> findGeneralFeedbacksWithPhotos();
    List<FeedbackDTO> findProjectFeedbacksWithPhotos(Long projectId);
    List<FeedbackDTO> findArticleFeedbacksWithPhotos(Long artigoId);
}