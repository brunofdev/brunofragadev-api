package com.brunofragadev.module.project.api.dto.response;

import java.util.List;

public record ProjectSetupDTO(
        String obs,
        List<SetupStepDTO> steps
) {}