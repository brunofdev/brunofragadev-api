package com.brunofragadev.module.project.infrastructure.adapter;

import com.brunofragadev.module.project.domain.entity.Project;
import com.brunofragadev.module.project.domain.repository.ProjectRepository;
import com.brunofragadev.module.project.infrastructure.persistence.SpringDataProjectRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProjectRepositoryAdapter implements ProjectRepository {

    private final SpringDataProjectRepository springRepository;

    public ProjectRepositoryAdapter(SpringDataProjectRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Project save(Project project) {
        return springRepository.save(project);
    }

    @Override
    public List<Project> findAll() {
        return springRepository.findAll();
    }

    @Override
    public Optional<Project> findById(Long id) {
        return springRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return springRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        springRepository.deleteById(id);
    }
}