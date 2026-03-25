package com.brunofragadev.shared.service;

import com.brunofragadev.module.feedback.domain.entity.FeedbackType;
import com.brunofragadev.module.project.domain.entity.Project;
import com.brunofragadev.module.project.domain.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ReferenceResolver {

    private final ProjectRepository projectRepository;

    public ReferenceResolver(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public String resolveName(FeedbackType type, Long referenceId) {
        if (type == FeedbackType.GERAL || referenceId == null) {
            return "Página geral";
        }

        if (type == FeedbackType.PROJETO) {
            return "Postado no projeto: " + projectRepository.findById(referenceId)
                    .map(Project::getTitle)
                    .orElse("Projeto não encontrado");
        }

        return "Referência desconhecida";
    }
}