package com.brunofragadev.module.feedback.api.controller;

import com.brunofragadev.module.feedback.application.usecase.CreateFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.DeleteFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.ListGeneralFeedbacksUseCase;
import com.brunofragadev.module.feedback.application.usecase.UpdateFeedbackUseCase;
import com.brunofragadev.module.feedback.api.dto.request.FeedbackCreateRequest;
import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.infrastructure.default_return_api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback/geral")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Feedback Geral", description = "Endpoints para gerenciar os comentários e avaliações gerais do portfólio")
public class GeneralFeedbackController {

    private final CreateFeedbackUseCase createFeedbackUseCase;
    private final ListGeneralFeedbacksUseCase listGeneralFeedbacksUseCase;
    private final UpdateFeedbackUseCase updateFeedbackUseCase;
    private final DeleteFeedbackUseCase deleteFeedbackUseCase;

    public GeneralFeedbackController(CreateFeedbackUseCase createFeedbackUseCase,
                                     ListGeneralFeedbacksUseCase listGeneralFeedbacksUseCase,
                                     UpdateFeedbackUseCase updateFeedbackUseCase,
                                     DeleteFeedbackUseCase deleteFeedbackUseCase) {
        this.createFeedbackUseCase = createFeedbackUseCase;
        this.listGeneralFeedbacksUseCase = listGeneralFeedbacksUseCase;
        this.updateFeedbackUseCase = updateFeedbackUseCase;
        this.deleteFeedbackUseCase = deleteFeedbackUseCase;
    }

    @PostMapping("/criar")
    @Operation(summary = "Criar feedback geral", description = "Registra uma nova avaliação geral no sistema. Requer autenticação.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Feedback criado com sucesso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição")
    public ResponseEntity<ApiResponse<FeedbackDTO>> createGeneralFeedback(
            @Valid @RequestBody FeedbackCreateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        FeedbackDTO createdFeedback = createFeedbackUseCase.execute(request, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feedback enviado com sucesso!", createdFeedback));
    }

    @GetMapping("/listar-todos")
    @Operation(summary = "Listar todos os feedbacks gerais", description = "Retorna uma lista com todas as avaliações gerais publicadas.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<ApiResponse<List<FeedbackDTO>>> listAllGeneralFeedbacks() {
        return ResponseEntity.ok(ApiResponse.success("Recursos encontrados", listGeneralFeedbacksUseCase.execute()));
    }

    @PutMapping("/atualizar/{idFeedback}")
    @Operation(summary = "Atualizar feedback geral", description = "Permite que o autor original ou um administrador atualize o conteúdo do feedback.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Feedback atualizado com sucesso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Usuário não tem permissão para editar este recurso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Feedback não encontrado")
    public ResponseEntity<ApiResponse<FeedbackDTO>> updateGeneralFeedback(
            @PathVariable Long idFeedback,
            @Valid @RequestBody FeedbackCreateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        FeedbackDTO updatedFeedback = updateFeedbackUseCase.execute(idFeedback, request, user);
        return ResponseEntity.ok(ApiResponse.success("Feedback atualizado com sucesso!", updatedFeedback));
    }

    @DeleteMapping("/excluir/{idFeedback}")
    @Operation(summary = "Excluir feedback geral", description = "Remove logicamente ou fisicamente uma avaliação do sistema.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Feedback excluído com sucesso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Usuário não tem permissão para excluir este recurso")
    public ResponseEntity<ApiResponse<Void>> deleteGeneralFeedback(
            @PathVariable Long idFeedback,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        deleteFeedbackUseCase.execute(idFeedback, user);
        return ResponseEntity.ok(ApiResponse.success("Feedback excluído com sucesso!", null));
    }
}