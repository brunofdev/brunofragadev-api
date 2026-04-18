package com.brunofragadev.module.project.application.usecase;

import com.brunofragadev.module.project.api.dto.response.ProjectResponse;
import com.brunofragadev.module.project.domain.entity.Project;
import com.brunofragadev.module.project.application.mapper.ProjectMapper;
import com.brunofragadev.module.project.domain.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetProjectByIdUseCase {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public GetProjectByIdUseCase(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Transactional(readOnly = true)
    public ProjectResponse returnDTO(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + id));
        return projectMapper.toDTO(project);
    }
    @Transactional(readOnly = true)
    public Project returnEntity(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + id));
    }
}