package com.brunofragadev.module.article.api;

import com.brunofragadev.infrastructure.default_return_api.ApiResponse;
import com.brunofragadev.module.article.application.GetArticleBySlugUseCase;
import com.brunofragadev.module.article.application.ListPublishedArticlesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/geral/artigos")
@Tag(name = "Artigos - Público", description = "Endpoints acessíveis para leitura e listagem de artigos publicados")
public class GeneralArticleController {
    private final ListPublishedArticlesUseCase listPublishedArticlesUseCase;
    private final GetArticleBySlugUseCase getArticleBySlugUseCase;

    public GeneralArticleController(ListPublishedArticlesUseCase listPublishedArticlesUseCase,
                                   GetArticleBySlugUseCase getArticleBySlugUseCase) {
        this.listPublishedArticlesUseCase = listPublishedArticlesUseCase;
        this.getArticleBySlugUseCase = getArticleBySlugUseCase;
    }

    @GetMapping("/listar-todos")
    @Operation(summary = "Listar todos os artigos publicados", description = "Retorna uma lista de artigos que estão disponíveis para leitura pública.")
    public ResponseEntity<ApiResponse<List<ArticleResponse>>> listPublished() {
        return ResponseEntity.ok(ApiResponse.success("Artigos encontrados", listPublishedArticlesUseCase.execute()));
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Ver detalhes do artigo", description = "Busca o conteúdo completo de um artigo através do seu slug amigável.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Artigo não encontrado")
    public ResponseEntity<ApiResponse<ArticleResponse>> getBySlug(@PathVariable String slug) {
        ArticleResponse response = getArticleBySlugUseCase.execute(slug);
        return ResponseEntity.ok(ApiResponse.success("Recurso encontrado", response));
    }
}

