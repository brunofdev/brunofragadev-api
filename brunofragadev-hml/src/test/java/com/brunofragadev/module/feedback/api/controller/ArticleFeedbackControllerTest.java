package com.brunofragadev.module.feedback.api.controller;

import com.brunofragadev.infrastructure.log.domain.repository.ErrorLogRepository;
import com.brunofragadev.infrastructure.security.JwtProvider;
import com.brunofragadev.module.auth.application.usecase.AuthorizationService;
import com.brunofragadev.module.feedback.api.dto.request.FeedbackCreateRequest;
import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.feedback.application.usecase.CreateFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.DeleteFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.ListArticleFeedbacksUseCase;
import com.brunofragadev.module.feedback.application.usecase.UpdateFeedbackUseCase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleFeedbackController.class)
@AutoConfigureMockMvc(addFilters = false)
class ArticleFeedbackControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CreateFeedbackUseCase createFeedbackUseCase;

    @MockitoBean
    ListArticleFeedbacksUseCase listArticleFeedbacksUseCase;

    @MockitoBean
    UpdateFeedbackUseCase updateFeedbackUseCase;

    @MockitoBean
    DeleteFeedbackUseCase deleteFeedbackUseCase;

    @MockitoBean
    ErrorLogRepository errorLogRepository;

    @MockitoBean
    AuthorizationService authorizationService;

    @MockitoBean
    JwtProvider jwtProvider;

    @Test
    @DisplayName("Deve retornar 201 ao criar um feedback com sucesso")
    void deveRetornar201AoCriarFeedback() throws Exception {
        FeedbackDTO mockResponse = mock(FeedbackDTO.class);
        when(createFeedbackUseCase.execute(any(FeedbackCreateRequest.class), any())).thenReturn(mockResponse);

        String validJsonRequest = """
                {
                    "descricao": "O artigo ficou sensacional e muito explicativo!",
                    "avaliacao": 5,
                    "tipoFeedback": "ARTIGO",
                    "referenciaId": 15
                }
                """;

        mockMvc.perform(post("/feedback/artigos/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Feedback enviado com sucesso!"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar um feedback com dados inválidos")
    void deveRetornar400AoCriarFeedbackInvalido() throws Exception {
        mockMvc.perform(post("/feedback/artigos/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 200 ao listar feedbacks de um artigo específico")
    void deveRetornar200AoListarFeedbacksDeArtigo() throws Exception {
        Long idArtigo = 15L;
        when(listArticleFeedbacksUseCase.execute(idArtigo)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/feedback/artigos/listar-todos/{idArtigo}", idArtigo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Recursos encontrados"))
                .andExpect(jsonPath("$.dados").isArray());
    }

    @Test
    @DisplayName("Deve retornar 200 ao atualizar um feedback com sucesso")
    void deveRetornar200AoAtualizarFeedback() throws Exception {
        Long idFeedback = 105L;
        FeedbackDTO mockResponse = mock(FeedbackDTO.class);
        when(updateFeedbackUseCase.execute(eq(idFeedback), any(FeedbackCreateRequest.class), any())).thenReturn(mockResponse);

        String validJsonRequest = """
                {
                    "descricao": "Atualizando minha avaliacao anterior com mais detalhes.",
                    "avaliacao": 4,
                    "tipoFeedback": "ARTIGO",
                    "referenciaId": 15
                }
                """;

        mockMvc.perform(put("/feedback/artigos/atualizar/{idFeedback}", idFeedback)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feedback do artigo atualizado com sucesso!"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar atualizar um feedback com dados inválidos")
    void deveRetornar400AoAtualizarFeedbackInvalido() throws Exception {
        Long idFeedback = 105L;

        mockMvc.perform(put("/feedback/artigos/atualizar/{idFeedback}", idFeedback)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 200 ao excluir um feedback com sucesso")
    void deveRetornar200AoExcluirFeedback() throws Exception {
        Long idFeedback = 105L;

        mockMvc.perform(delete("/feedback/artigos/excluir/{idFeedback}", idFeedback)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feedback do artigo excluído com sucesso!"));

        verify(deleteFeedbackUseCase).execute(eq(idFeedback), any());
    }
}