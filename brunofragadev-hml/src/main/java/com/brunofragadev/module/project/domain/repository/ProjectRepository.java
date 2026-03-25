package com.brunofragadev.module.project.domain.repository;

import com.brunofragadev.module.project.domain.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Project save(Project project);
    List<Project> findAll();
    Optional<Project> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
}