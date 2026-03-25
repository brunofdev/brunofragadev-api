package com.brunofragadev.module.project.api.dto.response;

public record ProjectImageDTO(
        Long id,
        String urlImagem,
        Integer ordemExibicao,
        Boolean isCapa,
        String legenda
) {}