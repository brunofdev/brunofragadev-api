package com.brunofragadev.infrastructure.log.api.dto.response;

import java.time.LocalDateTime;

public record ErrorLogResponse(
        Long id,
        LocalDateTime dataHora,
        String endpoint,
        String metodoHttp,
        String mensagemResumo,
        String stackTraceCompleta,
        String usuarioLogado
) {}
