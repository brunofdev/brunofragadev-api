package com.brunofragadev.module.project.infrastructure.adapter;

import com.brunofragadev.module.feedback.domain.port.FeedbackProjectPort;
import com.brunofragadev.module.project.application.usecase.GetProjectByIdUseCase;
import com.brunofragadev.module.project.domain.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectFeedbackAdapter implements FeedbackProjectPort {

    private final GetProjectByIdUseCase getProjectByIdUseCase;

    public ProjectFeedbackAdapter(GetProjectByIdUseCase getProjectByIdUseCase) {
        this.getProjectByIdUseCase = getProjectByIdUseCase;
    }

    @Override
    public String resolveName(Long id){
        Project project = getProjectByIdUseCase.returnEntity(id);
        return project.getTitle();
    }
}
