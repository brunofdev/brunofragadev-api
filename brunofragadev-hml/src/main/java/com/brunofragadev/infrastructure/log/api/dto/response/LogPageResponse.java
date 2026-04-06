package com.brunofragadev.infrastructure.log.api.dto.response;

import java.util.List;

public record LogPageResponse(
        List<ErrorLogResponse> logs,
        int paginaAtual,
        int totalPaginas,
        long totalElementos
) {}