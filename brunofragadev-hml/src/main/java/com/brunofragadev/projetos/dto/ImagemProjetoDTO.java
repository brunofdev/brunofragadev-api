package com.brunofragadev.projetos.dto;

public record ImagemProjetoDTO(
        Long id,
        String urlImagem,
        Integer ordemExibicao,
        Boolean isCapa,
        String legenda
) {}