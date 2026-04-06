package com.brunofragadev.infrastructure.log.api.controller;

import com.brunofragadev.infrastructure.log.application.usecase.GetPaginatedLogsUseCase;
import com.brunofragadev.infrastructure.log.api.dto.response.LogPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/paineladm/logs")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Logs - Privado", description = "Monitoramento e auditoria de erros do sistema (Apenas Administradores)")
public class LogController {

    private final GetPaginatedLogsUseCase getPaginatedLogsUseCase;

    public LogController(GetPaginatedLogsUseCase getPaginatedLogsUseCase) {
        this.getPaginatedLogsUseCase = getPaginatedLogsUseCase;
    }

    @GetMapping
    @Operation(
            summary = "Listar logs de erro do sistema",
            description = "Retorna uma lista paginada com as caixas-pretas (stack traces) dos erros inesperados ocorridos no servidor."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logs recuperados com sucesso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Usuário não possui permissão (Requer ADMIN3)")
    public ResponseEntity<LogPageResponse> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        LogPageResponse response = getPaginatedLogsUseCase.execute(page, size);
        return ResponseEntity.ok(response);
    }
}