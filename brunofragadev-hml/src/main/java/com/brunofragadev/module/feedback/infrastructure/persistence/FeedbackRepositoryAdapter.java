package com.brunofragadev.module.feedback.infrastructure.persistence;

import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.feedback.domain.entity.Feedback;
import com.brunofragadev.module.feedback.domain.repository.FeedbackRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FeedbackRepositoryAdapter implements FeedbackRepository {

    private final SpringDataFeedbackRepository springRepository;

    public FeedbackRepositoryAdapter(SpringDataFeedbackRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Feedback save(Feedback feedback) {
        return springRepository.save(feedback);
    }

    @Override
    public Optional<Feedback> findById(Long id) {
        return springRepository.findById(id);
    }

    @Override
    public void delete(Feedback feedback) {
        springRepository.delete(feedback);
    }

    @Override
    public List<FeedbackDTO> findGeneralFeedbacksWithPhotos() {
        return springRepository.findGeneralFeedbacksWithPhotos();
    }

    @Override
    public List<FeedbackDTO> findProjectFeedbacksWithPhotos(Long projectId) {
        return springRepository.findProjectFeedbacksWithPhotos(projectId);
    }

    @Override
    public List<Feedback> findAllFeedbackByProjectReferenceId(Long referenceId){
        return springRepository.findAllByReferenciaId(referenceId);
    }
    @Override
    public void  deleteAllByReferenceId (Long referenciaId){
        springRepository.deleteAllByReferenciaId(referenciaId);
    }

    @Override
    public List<FeedbackDTO> findArticleFeedbacksWithPhotos(Long artigoId) {
        return springRepository.findArticleFeedbacksWithPhotos(artigoId);
    }
}