package com.brunofragadev.module.project.infrastructure.mapper;

import com.brunofragadev.module.project.api.dto.request.ProjectRequest;
import com.brunofragadev.module.project.api.dto.response.*;
import com.brunofragadev.module.project.domain.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectMapper {

    public Project toEntity(ProjectRequest request, Project project) {
        project.setTitle(request.title());
        project.setDescription(request.description());
        project.setVideo(request.video());
        project.setStatus(request.status());
        project.setPapel(request.papel());
        project.setDataProjeto(request.dataProjeto());
        project.setRepositorioUrl(request.repositorioUrl());
        project.setLiveUrl(request.liveUrl());

        if (request.techs() != null) {
            TechnicalSheet technicalSheet = new TechnicalSheet();
            technicalSheet.setLinguagem(request.techs().linguagem());
            technicalSheet.setParadigma(request.techs().paradigma());
            technicalSheet.setFramework(request.techs().framework());
            technicalSheet.setBibliotecas(request.techs().bibliotecas());
            technicalSheet.setInfraestrutura(request.techs().infraestrutura());
            project.setTechs(technicalSheet);
        }

        if (request.setup() != null) {
            ProjectSetup setup = new ProjectSetup();
            setup.setObs(request.setup().obs());

            if (request.setup().steps() != null) {
                List<SetupStep> stepsList = request.setup().steps().stream().map(stepRequest -> {
                    SetupStep step = new SetupStep();
                    step.setNum(stepRequest.num());
                    step.setText(stepRequest.text());
                    step.setCmd(stepRequest.cmd());
                    return step;
                }).toList();
                setup.setSteps(stepsList);
            }
            project.setSetup(setup);
        }

        if (request.galeria() != null) {
            List<ProjectImage> images = request.galeria().stream().map(imageRequest -> {
                ProjectImage projectImage = new ProjectImage();
                projectImage.setUrlImagem(imageRequest.urlImagem());
                projectImage.setOrdemExibicao(imageRequest.ordemExibicao());
                projectImage.setIsCapa(imageRequest.isCapa());
                projectImage.setLegenda(imageRequest.legenda());

                return projectImage;
            }).toList();
            project.setGaleria(images);
        }

        return project;
    }

    public ProjectResponse toDTO(Project project) {
        TechnicalSheetDTO techsDTO = project.getTechs() != null ? new TechnicalSheetDTO(
                project.getTechs().getLinguagem(), project.getTechs().getParadigma(),
                project.getTechs().getFramework(), project.getTechs().getBibliotecas(),
                project.getTechs().getInfraestrutura()
        ) : null;

        ProjectSetupDTO setupDTO = null;
        if (project.getSetup() != null) {
            List<SetupStepDTO> stepsDTOList = project.getSetup().getSteps().stream()
                    .map(step -> new SetupStepDTO(step.getNum(), step.getText(), step.getCmd()))
                    .toList();
            setupDTO = new ProjectSetupDTO(project.getSetup().getObs(), stepsDTOList);
        }

        List<ProjectImageDTO> galleryDTOList = project.getGaleria().stream()
                .map(image -> new ProjectImageDTO(
                        image.getId(),
                        image.getUrlImagem(),
                        image.getOrdemExibicao(),
                        image.getIsCapa(),
                        image.getLegenda()
                ))
                .toList();

        return new ProjectResponse(
                project.getId(), project.getTitle(), project.getDescription(),
                project.getVideo(), project.getStatus(), project.getPapel(),
                project.getDataProjeto(), project.getRepositorioUrl(), project.getLiveUrl(),
                techsDTO, setupDTO, galleryDTOList
        );
    }
}