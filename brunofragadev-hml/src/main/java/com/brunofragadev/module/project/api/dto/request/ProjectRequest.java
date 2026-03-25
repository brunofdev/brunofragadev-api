package com.brunofragadev.module.project.api.dto.request;

import com.brunofragadev.module.project.api.dto.response.ProjectImageDTO;
import com.brunofragadev.module.project.api.dto.response.ProjectSetupDTO;
import com.brunofragadev.module.project.api.dto.response.TechnicalSheetDTO;

import java.util.List;

public record ProjectRequest(
        String title,
        String description,
        String video,
        String status,
        String papel,
        String dataProjeto,
        String repositorioUrl,
        String liveUrl,
        TechnicalSheetDTO techs,
        ProjectSetupDTO setup,
        List<ProjectImageDTO> galeria
) {}