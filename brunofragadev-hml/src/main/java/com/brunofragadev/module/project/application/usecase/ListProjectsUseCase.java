package com.brunofragadev.module.project.application.usecase;

import com.brunofragadev.module.project.api.dto.response.ProjectResponse;
import com.brunofragadev.module.project.application.mapper.ProjectMapper;
import com.brunofragadev.module.project.domain.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListProjectsUseCase {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ListProjectsUseCase(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> execute() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toDTO)
                .toList();
    }
}