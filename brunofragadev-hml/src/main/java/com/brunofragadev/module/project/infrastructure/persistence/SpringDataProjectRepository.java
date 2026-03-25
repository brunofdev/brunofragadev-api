package com.brunofragadev.module.project.infrastructure.persistence;

import com.brunofragadev.module.project.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProjectRepository extends JpaRepository<Project, Long> {
}