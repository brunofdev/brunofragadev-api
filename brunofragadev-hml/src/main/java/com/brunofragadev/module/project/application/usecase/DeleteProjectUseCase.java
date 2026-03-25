package com.brunofragadev.module.project.application.usecase;

import com.brunofragadev.module.feedback.application.usecase.RemoveAllProjectFeedbacksUseCase;
import com.brunofragadev.module.project.domain.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteProjectUseCase {

    private final ProjectRepository projectRepository;
    private final RemoveAllProjectFeedbacksUseCase removeAllProjectFeedbacksUseCase;

    public DeleteProjectUseCase(ProjectRepository projectRepository, RemoveAllProjectFeedbacksUseCase removeAllProjectFeedbacksUseCase) {
        this.projectRepository = projectRepository;
        this.removeAllProjectFeedbacksUseCase = removeAllProjectFeedbacksUseCase;
    }

    @Transactional
    public void execute(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException("Project not found with ID: " + id);
        }
        projectRepository.deleteById(id);
        removeAllProjectFeedbacksUseCase.execute(id);
    }
}