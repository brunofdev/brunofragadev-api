package com.brunofragadev.module.article.api.controller;

import com.brunofragadev.infrastructure.default_return_api.ApiResponse;
import com.brunofragadev.module.article.api.dto.response.ArticleResponse;
import com.brunofragadev.module.article.api.dto.response.ArticleSummaryResponse;
import com.brunofragadev.module.article.application.usecase.GetArticleBySlugUseCase;
import com.brunofragadev.module.article.application.usecase.ListAllArticlesUseCase;
import com.brunofragadev.module.article.application.usecase.ListLatestArticlesUseCase;
import com.brunofragadev.module.article.application.usecase.ListPublishedArticlesUseCase;
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
    private final ListLatestArticlesUseCase listLatestArticlesUseCase;
    private final ListAllArticlesUseCase listAllArticlesUseCase;

    public GeneralArticleController(ListPublishedArticlesUseCase listPublishedArticlesUseCase,
                                   GetArticleBySlugUseCase getArticleBySlugUseCase,
                                   ListLatestArticlesUseCase listLatestArticlesUseCase,
                                   ListAllArticlesUseCase listAllArticlesUseCase) {
        this.listPublishedArticlesUseCase = listPublishedArticlesUseCase;
        this.getArticleBySlugUseCase = getArticleBySlugUseCase;
        this.listLatestArticlesUseCase = listLatestArticlesUseCase;
        this.listAllArticlesUseCase = listAllArticlesUseCase;
    }

    @GetMapping("/listar-todos")
    @Operation(summary = "Listar todos os artigos existentes", description = "Retorna uma lista de artigos que estão disponíveis independente do status.")
    public ResponseEntity<ApiResponse<List<ArticleResponse>>> listAll() {
        return ResponseEntity.ok(ApiResponse.success("Artigos encontrados", listAllArticlesUseCase.execute()));
    }
    @GetMapping("/listar-todos-publicados")
    @Operation(summary = "Listar todos os artigos com status de publicados", description = "Retorna uma lista de artigos que estão disponíveis para leitura pública.")
    public ResponseEntity<ApiResponse<List<ArticleResponse>>> listAllPublished() {
        return ResponseEntity.ok(ApiResponse.success("Artigos encontrados", listPublishedArticlesUseCase.execute()));
    }
    @GetMapping("/listar-ultimos-publicados")
    @Operation(summary = "Listar ultimos publicados", description = "Retorna uma lista de 5 artigos que foram publicados por ultimo.")
    public ResponseEntity<ApiResponse<List<ArticleSummaryResponse>>> listLastPublished() {
        return ResponseEntity.ok(ApiResponse.success("Artigos encontrados", listLatestArticlesUseCase.execute()));
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Ver detalhes do artigo", description = "Busca o conteúdo completo de um artigo através do seu slug amigável.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Artigo não encontrado")
    public ResponseEntity<ApiResponse<ArticleResponse>> getBySlug(@PathVariable String slug) {
        ArticleResponse response = getArticleBySlugUseCase.execute(slug);
        return ResponseEntity.ok(ApiResponse.success("Recurso encontrado", response));
    }

}

