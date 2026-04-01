package com.brunofragadev.module.feedback.api.controller;

import com.brunofragadev.module.feedback.application.usecase.CreateFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.DeleteFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.ListArticleFeedbacksUseCase; // <-- Assumindo este nome
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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/feedback/artigos") // 👈 Endereço específico para Artigos
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Feedback de Artigos", description = "Endpoints específicos para avaliações atreladas aos artigos do blog")
public class ArticleFeedbackController {

    private final CreateFeedbackUseCase createFeedbackUseCase;
    private final ListArticleFeedbacksUseCase listArticleFeedbacksUseCase;
    private final UpdateFeedbackUseCase updateFeedbackUseCase;
    private final DeleteFeedbackUseCase deleteFeedbackUseCase;

    public ArticleFeedbackController(CreateFeedbackUseCase createFeedbackUseCase,
                                     ListArticleFeedbacksUseCase listArticleFeedbacksUseCase,
                                     UpdateFeedbackUseCase updateFeedbackUseCase,
                                     DeleteFeedbackUseCase deleteFeedbackUseCase) {
        this.createFeedbackUseCase = createFeedbackUseCase;
        this.listArticleFeedbacksUseCase = listArticleFeedbacksUseCase;
        this.updateFeedbackUseCase = updateFeedbackUseCase;
        this.deleteFeedbackUseCase = deleteFeedbackUseCase;
    }

    @PostMapping("/criar")
    @Operation(summary = "Criar feedback de artigo", description = "Registra uma nova avaliação vinculada especificamente a um artigo.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Feedback do artigo criado com sucesso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Artigo de referência não encontrado")
    public ResponseEntity<ApiResponse<FeedbackDTO>> createArticleFeedback(
            @Valid @RequestBody FeedbackCreateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        FeedbackDTO createdFeedback = createFeedbackUseCase.execute(request, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feedback enviado com sucesso!", createdFeedback));
    }

    @GetMapping("/listar-todos/{idArtigo}")
    @Operation(summary = "Listar feedbacks de um artigo", description = "Retorna todas as avaliações vinculadas ao ID de um artigo específico.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<ApiResponse<List<FeedbackDTO>>> listAllArticleFeedbacks(
            @Parameter(description = "ID único do artigo") @PathVariable Long idArtigo) {
        return ResponseEntity.ok(ApiResponse.success("Recursos encontrados", listArticleFeedbacksUseCase.execute(idArtigo)));
    }

    @PutMapping("/atualizar/{idFeedback}")
    @Operation(summary = "Atualizar feedback de artigo", description = "Edita uma avaliação existente de um artigo.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Feedback atualizado com sucesso")
    public ResponseEntity<ApiResponse<FeedbackDTO>> updateArticleFeedback(
            @PathVariable Long idFeedback,
            @Valid @RequestBody FeedbackCreateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        FeedbackDTO updatedFeedback = updateFeedbackUseCase.execute(idFeedback, request, user);
        return ResponseEntity.ok(ApiResponse.success("Feedback do artigo atualizado com sucesso!", updatedFeedback));
    }

    @DeleteMapping("/excluir/{idFeedback}")
    @Operation(summary = "Excluir feedback de artigo", description = "Remove uma avaliação atrelada a um artigo.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Feedback excluído com sucesso")
    public ResponseEntity<ApiResponse<Void>> deleteArticleFeedback(
            @PathVariable Long idFeedback,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        deleteFeedbackUseCase.execute(idFeedback, user);
        return ResponseEntity.ok(ApiResponse.success("Feedback do artigo excluído com sucesso!", null));
    }
}