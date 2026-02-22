package com.brunofragadev.feedback.controller;


import com.brunofragadev.feedback.service.FeedbackServico;
import com.brunofragadev.feedback.dto.CriarFeedbackDTO;
import com.brunofragadev.feedback.dto.FeedbackDTO;
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
@RequestMapping("/feedback")
@SecurityRequirement(name = "bearerAuth")
public class FeedbackController {

        @Autowired
        private FeedbackServico feedbackServico;

       @PostMapping("/criar")
        public ResponseEntity<ApiResponse<FeedbackDTO>> criarFeedback
               (
                @RequestBody CriarFeedbackDTO dto,
                @AuthenticationPrincipal Usuario usuario
               ) {
           FeedbackDTO feedbackCriado = feedbackServico.criarFeedback(dto, usuario);
           return ResponseEntity
                   .status(HttpStatus.CREATED)
                   .body(ApiResponse.success("Feedback enviado com sucesso!", feedbackCriado));

       }
       @GetMapping("/listar-todos")
        public ResponseEntity<ApiResponse<List<FeedbackDTO>>> listarTodosFeedbacks(){
           return ResponseEntity.ok(ApiResponse.success("Recursos encontrados", feedbackServico.listarFeedbacks()));
       }
}
