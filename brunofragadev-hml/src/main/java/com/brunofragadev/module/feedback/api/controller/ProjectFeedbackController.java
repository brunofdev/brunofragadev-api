package com.brunofragadev.module.feedback.api.controller;

import com.brunofragadev.module.feedback.application.usecase.CreateFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.DeleteFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.ListProjectFeedbacksUseCase;
import com.brunofragadev.module.feedback.application.usecase.UpdateFeedbackUseCase;
import com.brunofragadev.module.feedback.api.dto.request.FeedbackCreateRequest;
import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.infrastructure.default_return_api.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/feedback/projetos")
@SecurityRequirement(name = "bearerAuth")
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
    public ResponseEntity<ApiResponse<FeedbackDTO>> createProjectFeedback(
            @Valid @RequestBody FeedbackCreateRequest request,
            @AuthenticationPrincipal User user) {
        FeedbackDTO createdFeedback = createFeedbackUseCase.execute(request, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feedback enviado com sucesso!", createdFeedback));
    }

    @GetMapping("/listar-todos/{idprojeto}")
    public ResponseEntity<ApiResponse<List<FeedbackDTO>>> listAllProjectFeedbacks(
            @PathVariable Long idprojeto) {
        return ResponseEntity.ok(ApiResponse.success("Recursos encontrados", listProjectFeedbacksUseCase.execute(idprojeto)));
    }

    @PutMapping("/atualizar/{idFeedback}")
    public ResponseEntity<ApiResponse<FeedbackDTO>> updateProjectFeedback(
            @PathVariable Long idFeedback,
            @Valid @RequestBody FeedbackCreateRequest request,
            @AuthenticationPrincipal User user) {
        FeedbackDTO updatedFeedback = updateFeedbackUseCase.execute(idFeedback, request, user);
        return ResponseEntity.ok(ApiResponse.success("Feedback do projeto atualizado com sucesso!", updatedFeedback));
    }

    @DeleteMapping("/excluir/{idFeedback}")
    public ResponseEntity<ApiResponse<Void>> deleteProjectFeedback(
            @PathVariable Long idFeedback,
            @AuthenticationPrincipal User user) {
        deleteFeedbackUseCase.execute(idFeedback, user);
        return ResponseEntity.ok(ApiResponse.success("Feedback do projeto excluído com sucesso!", null));
    }
}