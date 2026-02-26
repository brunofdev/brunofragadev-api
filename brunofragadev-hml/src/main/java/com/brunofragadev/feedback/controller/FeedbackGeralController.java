package com.brunofragadev.feedback.controller;

import com.brunofragadev.feedback.service.FeedbackServico;
import com.brunofragadev.feedback.dto.entrada.CriarFeedbackDTO;
import com.brunofragadev.feedback.dto.saida.FeedbackDTO;
import com.brunofragadev.usuarios.entity.Usuario;
import com.brunofragadev.utils.retorno_padrao_api.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/feedback/geral") // 👈 Rota base focada apenas no Geral
@SecurityRequirement(name = "bearerAuth")
public class FeedbackGeralController {

    @Autowired
    private FeedbackServico feedbackServico;

    @PostMapping("/criar")
    public ResponseEntity<ApiResponse<FeedbackDTO>> criarFeedbackTipoGeral(
            @RequestBody CriarFeedbackDTO dto,
            @AuthenticationPrincipal Usuario usuario) {
        FeedbackDTO feedbackCriado = feedbackServico.criarFeedback(dto, usuario);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feedback enviado com sucesso!", feedbackCriado));
    }

    @GetMapping("/listar-todos")
    public ResponseEntity<ApiResponse<List<FeedbackDTO>>> listarTodosTipoGeralFeedbacks() {
        return ResponseEntity.ok(ApiResponse.success("Recursos encontrados", feedbackServico.listarFeedbacksTipoGeral()));
    }

    @PutMapping("/atualizar/{idFeedback}")
    public ResponseEntity<ApiResponse<FeedbackDTO>> atualizarFeedbackTipoGeral(
            @PathVariable Long idFeedback,
            @RequestBody CriarFeedbackDTO dto,
            @AuthenticationPrincipal Usuario usuario) {
        FeedbackDTO feedbackAtualizado = feedbackServico.atualizarFeedback(idFeedback, dto, usuario);
        return ResponseEntity.ok(ApiResponse.success("Feedback atualizado com sucesso!", feedbackAtualizado));
    }

    @DeleteMapping("/excluir/{idFeedback}")
    public ResponseEntity<ApiResponse<Void>> excluirFeedbackTipoGeral(
            @PathVariable Long idFeedback,
            @AuthenticationPrincipal Usuario usuario) {
        feedbackServico.excluirFeedback(idFeedback, usuario);
        return ResponseEntity.ok(ApiResponse.success("Feedback excluído com sucesso!", null));
    }
}