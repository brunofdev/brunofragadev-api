package com.brunofragadev.infrastructure.log.application.mapper;

import com.brunofragadev.infrastructure.log.domain.entity.ErrorLog;
import com.brunofragadev.infrastructure.log.api.dto.response.ErrorLogResponse;
import com.brunofragadev.infrastructure.log.api.dto.response.LogPage;
import com.brunofragadev.infrastructure.log.api.dto.response.LogPageResponse;

import java.util.List;

public class ErrorLogMapper {
    public static ErrorLogResponse toResponse(ErrorLog log) {
        return new ErrorLogResponse(
                log.getId(),
                log.getDataHora(),
                log.getEndpoint(),
                log.getMetodoHttp(),
                log.getMensagemResumo(),
                log.getStackTraceCompleta(),
                log.getUsuarioLogado()
        );
    }

    public static LogPageResponse toPageResponse(LogPage page) {
        List<ErrorLogResponse> mappedLogs = page.logs().stream()
                .map(ErrorLogMapper::toResponse)
                .toList();

        return new LogPageResponse(
                mappedLogs,
                page.paginaAtual(),
                page.totalPaginas(),
                page.totalElementos()
        );
    }
}


