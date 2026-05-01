package com.brunofragadev.module.feedback.api.controller;

import com.brunofragadev.module.feedback.application.usecase.CreateFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.DeleteFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.ListProjectFeedbacksUseCase;
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
@RequestMapping("/feedback/projetos")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Feedback de Projetos", description = "Endpoints específicos para avaliações atreladas a projetos do portfólio")
public class ProjectFeedbackController {

    private final CreateFeedbackUseCase createFeedbackUseCase;
    private final ListProjectFeedbacksUseCase listProjectFeedbacksUseCase;
    private final UpdateFeedbackUseCase updateFeedbackUseCase;
    private final DeleteFeedbackUseCase deleteFeedbackUseCase;

    public ProjectFeedbackController(CreateFeedbackUseCase createFeedbackUseCase,
                                     ListProjectFeedbacksUseCase listProjectFeedbacksUseCase,
                                     UpdateFeedbackUseCase updateFeedbackUseCase,
                                     DeleteFeedbackUseCase deleteFeedbackUseCase) {
        this.createFeedbackUseCase = createFeedbackUseCase;
        this.listProjectFeedbacksUseCase = listProjectFeedbacksUseCase;
        this.updateFeedbackUseCase = updateFeedbackUseCase;
        this.deleteFeedbackUseCase = deleteFeedbackUseCase;
    }

    @PostMapping("/criar")
    @Operation(summary = "Criar feedback de projeto", description = "Registra uma nova avaliação vinculada especificamente a um projeto.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Feedback do projeto criado com sucesso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Projeto de referência não encontrado")
    public ResponseEntity<ApiResponse<FeedbackDTO>> createProjectFeedback(
            @Valid @RequestBody FeedbackCreateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        FeedbackDTO createdFeedback = createFeedbackUseCase.execute(request, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feedback enviado com sucesso!", createdFeedback));
    }

    @GetMapping("/listar-todos/{idprojeto}")
    @Operation(summary = "Listar feedbacks de um projeto", description = "Retorna todas as avaliações vinculadas ao ID de um projeto específico.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<ApiResponse<List<FeedbackDTO>>> listAllProjectFeedbacks(
            @Parameter(description = "ID único do projeto") @PathVariable Long idprojeto) {
        return ResponseEntity.ok(ApiResponse.success("Recursos encontrados", listProjectFeedbacksUseCase.execute(idprojeto)));
    }

    @PutMapping("/atualizar/{idFeedback}")
    @Operation(summary = "Atualizar feedback de projeto", description = "Edita uma avaliação existente de um projeto.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Feedback atualizado com sucesso")
    public ResponseEntity<ApiResponse<FeedbackDTO>> updateProjectFeedback(
            @PathVariable Long idFeedback,
            @Valid @RequestBody FeedbackCreateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        FeedbackDTO updatedFeedback = updateFeedbackUseCase.execute(idFeedback, request, user);
        return ResponseEntity.ok(ApiResponse.success("Feedback do projeto atualizado com sucesso!", updatedFeedback));
    }

    @DeleteMapping("/excluir/{idFeedback}")
    @Operation(summary = "Excluir feedback de projeto", description = "Remove uma avaliação atrelada a um projeto.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Feedback excluído com sucesso")
    public ResponseEntity<ApiResponse<Void>> deleteProjectFeedback(
            @PathVariable Long idFeedback,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        deleteFeedbackUseCase.execute(idFeedback, user);
        return ResponseEntity.ok(ApiResponse.success("Feedback do projeto excluído com sucesso!", null));
    }
}