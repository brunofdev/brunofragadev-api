package com.brunofragadev.shared.application.service;
import com.brunofragadev.module.feedback.domain.entity.FeedbackType;
import com.brunofragadev.module.project.domain.entity.Project;
import com.brunofragadev.module.project.domain.repository.ProjectRepository;
import com.brunofragadev.shared.domain.repository.ReferenceResolverInterface;
import org.springframework.stereotype.Component;


/**
 * Resolve referências do tipo {@link FeedbackType#PROJETO}.
 * Busca o título do projeto pelo ID usando {@link ProjectRepository}.
 */
@Component
public class ProjectReferenceResolver implements ReferenceResolverInterface {

    private final ProjectRepository projectRepository;

    public ProjectReferenceResolver(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public FeedbackType getType(){
        return FeedbackType.PROJETO;
    }

    @Override
    public String resolveName(Long referenceId) {
            return "Postado no Projeto: " + projectRepository.findById(referenceId)
                    .map(Project::getTitle)
                    .orElse("Projeto não localizado");
    }
}
