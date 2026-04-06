package com.brunofragadev.infrastructure.log.api.dto.response;

import com.brunofragadev.infrastructure.log.domain.entity.ErrorLog;

import java.util.List;

public record LogPage(
        List<ErrorLog> logs,
        int paginaAtual,
        int totalPaginas,
        long totalElementos
) {}