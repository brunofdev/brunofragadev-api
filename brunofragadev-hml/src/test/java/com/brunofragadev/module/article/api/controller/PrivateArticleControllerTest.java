package com.brunofragadev.module.article.api.controller;

import com.brunofragadev.infrastructure.log.domain.repository.ErrorLogRepository;
import com.brunofragadev.infrastructure.security.JwtProvider;
import com.brunofragadev.module.article.api.dto.request.ArticleRequest;
import com.brunofragadev.module.article.api.dto.response.ArticleResponse;
import com.brunofragadev.module.article.application.usecase.CreateArticleUseCase;
import com.brunofragadev.module.article.application.usecase.UpdateArticleUseCase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrivateArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
class PrivateArticleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CreateArticleUseCase createArticleUseCase;

    @MockitoBean
    UpdateArticleUseCase updateArticleUseCase;

    @MockitoBean
    ErrorLogRepository errorLogRepository;

    @MockitoBean
    AuthorizationService authorizationService;

    @MockitoBean
    JwtProvider jwtProvider;

    @Test
    @DisplayName("Deve retornar 201 ao criar um novo artigo com sucesso")
    void deveRetornar201AoCriarArtigo() throws Exception {
        ArticleResponse mockResponse = mock(ArticleResponse.class);
        when(createArticleUseCase.execute(any(ArticleRequest.class))).thenReturn(mockResponse);

        String validJsonRequest = """
                {
                    "title": "Microsserviços com Spring Boot",
                    "subtitle": "Aprenda a escalar sua arquitetura",
                    "slug": "microsservicos-com-spring-boot",
                    "tags": ["Java", "Backend"],
                    "coverImage": "https://res.cloudinary.com/imagem.jpg",
                    "fontFamily": "Instrument Serif",
                    "contentHtml": "<p>Conteúdo do artigo aqui</p>",
                    "status": "PUBLICADO",
                    "contentJson": {"type": "doc", "content": []}
                }
                """;

        mockMvc.perform(post("/paineladm/artigos/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Artigo criado com sucesso!"));
    }

    @Test
    @DisplayName("Deve retornar 200 ao atualizar um artigo com sucesso")
    void deveRetornar200AoAtualizarArtigo() throws Exception {
        Long id = 1L;
        ArticleResponse mockResponse = mock(ArticleResponse.class);
        when(updateArticleUseCase.execute(eq(id), any(ArticleRequest.class))).thenReturn(mockResponse);

        String validJsonRequest = """
                {
                    "title": "Artigo Atualizado Spring",
                    "subtitle": "Subtítulo atualizado",
                    "slug": "artigo-atualizado-spring",
                    "tags": ["Tech", "Atualização"],
                    "coverImage": "https://res.cloudinary.com/nova-imagem.jpg",
                    "fontFamily": "Roboto",
                    "contentHtml": "<p>Novo conteúdo</p>",
                    "status": "RASCUNHO",
                    "contentJson": {"type": "doc", "content": []}
                }
                """;

        mockMvc.perform(put("/paineladm/artigos/atualizar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Artigo atualizado com sucesso!"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar um artigo com corpo vazio ou inválido")
    void deveRetornar400AoCriarArtigoInvalido() throws Exception {
        mockMvc.perform(post("/paineladm/artigos/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar atualizar um artigo com corpo vazio ou inválido")
    void deveRetornar400AoAtualizarArtigoInvalido() throws Exception {
        Long id = 1L;

        mockMvc.perform(put("/paineladm/artigos/atualizar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}