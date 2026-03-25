package com.brunofragadev.module.project.application.usecase;

import com.brunofragadev.module.project.api.dto.request.ProjectRequest;
import com.brunofragadev.module.project.api.dto.response.ProjectResponse;
import com.brunofragadev.module.project.domain.entity.Project;
import com.brunofragadev.module.project.infrastructure.mapper.ProjectMapper;
import com.brunofragadev.module.project.domain.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateProjectUseCase {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public UpdateProjectUseCase(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Transactional
    public ProjectResponse execute(Long id, ProjectRequest request) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + id));

        projectMapper.toEntity(request, existingProject);
        Project updatedProject = projectRepository.save(existingProject);

        return projectMapper.toDTO(updatedProject);
    }
}