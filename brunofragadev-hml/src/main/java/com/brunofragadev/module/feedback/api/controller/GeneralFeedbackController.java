package com.brunofragadev.module.feedback.api.controller;

import com.brunofragadev.module.feedback.application.usecase.CreateFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.DeleteFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.ListGeneralFeedbacksUseCase;
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
@RequestMapping("/feedback/geral")
@SecurityRequirement(name = "bearerAuth")
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
    public ResponseEntity<ApiResponse<FeedbackDTO>> createGeneralFeedback(
            @Valid @RequestBody FeedbackCreateRequest request,
            @AuthenticationPrincipal User user) {
        FeedbackDTO createdFeedback = createFeedbackUseCase.execute(request, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feedback enviado com sucesso!", createdFeedback));
    }

    @GetMapping("/listar-todos")
    public ResponseEntity<ApiResponse<List<FeedbackDTO>>> listAllGeneralFeedbacks() {
        return ResponseEntity.ok(ApiResponse.success("Recursos encontrados", listGeneralFeedbacksUseCase.execute()));
    }

    @PutMapping("/atualizar/{idFeedback}")
    public ResponseEntity<ApiResponse<FeedbackDTO>> updateGeneralFeedback(
            @PathVariable Long idFeedback,
            @Valid @RequestBody FeedbackCreateRequest request,
            @AuthenticationPrincipal User user) {
        FeedbackDTO updatedFeedback = updateFeedbackUseCase.execute(idFeedback, request, user);
        return ResponseEntity.ok(ApiResponse.success("Feedback atualizado com sucesso!", updatedFeedback));
    }

    @DeleteMapping("/excluir/{idFeedback}")
    public ResponseEntity<ApiResponse<Void>> deleteGeneralFeedback(
            @PathVariable Long idFeedback,
            @AuthenticationPrincipal User user) {
        deleteFeedbackUseCase.execute(idFeedback, user);
        return ResponseEntity.ok(ApiResponse.success("Feedback excluído com sucesso!", null));
    }
}