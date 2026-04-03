package com.brunofragadev.shared.service;

import com.brunofragadev.module.article.domain.entity.Article;
import com.brunofragadev.module.article.infrastructure.persistence.ArticleRepositoryAdapter;
import com.brunofragadev.module.feedback.domain.entity.FeedbackType;
import com.brunofragadev.module.project.domain.entity.Project;
import com.brunofragadev.module.project.domain.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ReferenceResolver {

    private final ProjectRepository projectRepository;
    private final ArticleRepositoryAdapter articleRepositoryAdapter;

    public ReferenceResolver(ProjectRepository projectRepository, ArticleRepositoryAdapter articleRepositoryAdapter) {
        this.projectRepository = projectRepository;
        this.articleRepositoryAdapter = articleRepositoryAdapter;
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
        if (type == FeedbackType.ARTIGO){
            return "Postado no Artigo: " + articleRepositoryAdapter.findById(referenceId)
                    .map(Article::getTitle)
                    .orElse("Artigo não localizado");
        }

        return "Referência desconhecida";
    }
}