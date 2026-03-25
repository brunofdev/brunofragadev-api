package com.brunofragadev.module.project.application.usecase;

import com.brunofragadev.module.project.api.dto.request.ProjectRequest;
import com.brunofragadev.module.project.api.dto.response.ProjectResponse;
import com.brunofragadev.module.project.domain.entity.Project;
import com.brunofragadev.module.project.infrastructure.mapper.ProjectMapper;
import com.brunofragadev.module.project.domain.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateProjectUseCase {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public CreateProjectUseCase(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Transactional
    public ProjectResponse execute(ProjectRequest request) {
        Project project = projectMapper.toEntity(request, new Project());
        Project savedProject = projectRepository.save(project);
        return projectMapper.toDTO(savedProject);
    }
}