package com.brunofragadev.module.article.api;

import com.brunofragadev.infrastructure.default_return_api.ApiResponse;
import com.brunofragadev.module.article.application.CreateArticleUseCase;
import com.brunofragadev.module.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/paineladm/artigos")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Artigos - Privado", description = "Gerenciamento administrativo de artigos (Criação, Edição, Exclusão)")
public class PrivateArticleController {

    private final CreateArticleUseCase createArticleUseCase;

    public PrivateArticleController(CreateArticleUseCase createArticleUseCase) {
        this.createArticleUseCase = createArticleUseCase;
    }

    @PostMapping("/criar")
    @Operation(summary = "Criar novo artigo", description = "Registra um novo artigo rascunho ou publicado. Requer permissão de administrador.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Artigo criado com sucesso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Usuário não possui permissão de editor")
    public ResponseEntity<ApiResponse<ArticleResponse>> createArticle(
            @Valid @RequestBody ArticleRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {

        ArticleResponse response = createArticleUseCase.execute(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Artigo criado com sucesso!", response));
    }
}