package com.brunofragadev.module.article.api.controller;

import com.brunofragadev.infrastructure.log.domain.repository.ErrorLogRepository;
import com.brunofragadev.infrastructure.security.JwtProvider;
import com.brunofragadev.module.article.api.dto.response.ArticleResponse;
import com.brunofragadev.module.article.application.usecase.GetArticleBySlugUseCase;
import com.brunofragadev.module.article.application.usecase.ListAllArticlesUseCase;
import com.brunofragadev.module.article.application.usecase.ListLatestArticlesUseCase;
import com.brunofragadev.module.article.application.usecase.ListPublishedArticlesUseCase;
import com.brunofragadev.module.article.domain.exception.ArticleBySlugNotFoundException;
import com.brunofragadev.module.auth.application.usecase.AuthorizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GeneralArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
class GeneralArticleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ListPublishedArticlesUseCase listPublishedArticlesUseCase;

    @MockitoBean
    GetArticleBySlugUseCase getArticleBySlugUseCase;

    @MockitoBean
    ListLatestArticlesUseCase listLatestArticlesUseCase;

    @MockitoBean
    ListAllArticlesUseCase listAllArticlesUseCase;

    @MockitoBean
    ErrorLogRepository errorLogRepository;

    @MockitoBean
    AuthorizationService authorizationService;

    @MockitoBean
    JwtProvider jwtProvider;

    @Test
    @DisplayName("Deve retornar 200 e a lista vazia ou preenchida ao buscar todos os artigos")
    void deveRetornar200AoListarTodosOsArtigos() throws Exception {
        when(listAllArticlesUseCase.execute()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/geral/artigos/listar-todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Artigos encontrados"))
                .andExpect(jsonPath("$.dados").isArray());
    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar todos os artigos publicados")
    void deveRetornar200AoListarTodosOsArtigosPublicados() throws Exception {
        when(listPublishedArticlesUseCase.execute()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/geral/artigos/listar-todos-publicados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Artigos encontrados"))
                .andExpect(jsonPath("$.dados").isArray());
    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar os ultimos artigos publicados")
    void deveRetornar200AoListarUltimosArtigosPublicados() throws Exception {
        when(listLatestArticlesUseCase.execute()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/geral/artigos/listar-ultimos-publicados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Artigos encontrados"))
                .andExpect(jsonPath("$.dados").isArray());
    }

    @Test
    @DisplayName("Deve retornar 200 e os detalhes do artigo ao buscar por um slug existente")
    void deveRetornar200AoBuscarArtigoPorSlug() throws Exception {
        String slug = "como-aprender-java";
        ArticleResponse mockResponse = mock(ArticleResponse.class);
        when(getArticleBySlugUseCase.execute(slug)).thenReturn(mockResponse);

        mockMvc.perform(get("/geral/artigos/{slug}", slug)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Recurso encontrado"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando buscar por um slug que não existe")
    void deveRetornar404QuandoArtigoPorSlugNaoExistir() throws Exception {
        String slugInexistente = "artigo-fantasma";
        when(getArticleBySlugUseCase.execute(slugInexistente))
                .thenThrow(new ArticleBySlugNotFoundException("Artigo não encontrado"));

        mockMvc.perform(get("/geral/artigos/{slug}", slugInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}