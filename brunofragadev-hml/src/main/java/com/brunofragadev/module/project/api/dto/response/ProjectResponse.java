package com.brunofragadev.module.project.api.dto.response;

import java.util.List;

public record ProjectResponse(
        Long id,
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