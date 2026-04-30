package com.brunofragadev.module.feedback.api.controller;

import com.brunofragadev.infrastructure.log.domain.repository.ErrorLogRepository;
import com.brunofragadev.infrastructure.security.JwtProvider;
import com.brunofragadev.module.auth.application.usecase.AuthorizationService;
import com.brunofragadev.module.feedback.api.dto.request.FeedbackCreateRequest;
import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.feedback.application.usecase.CreateFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.DeleteFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.ListGeneralFeedbacksUseCase;
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

@WebMvcTest(GeneralFeedbackController.class)
@AutoConfigureMockMvc(addFilters = false)
class GeneralFeedbackControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CreateFeedbackUseCase createFeedbackUseCase;

    @MockitoBean
    ListGeneralFeedbacksUseCase listGeneralFeedbacksUseCase;

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
    @DisplayName("Deve retornar 201 ao criar um feedback geral com sucesso")
    void deveRetornar201AoCriarFeedbackGeral() throws Exception {
        FeedbackDTO mockResponse = mock(FeedbackDTO.class);
        when(createFeedbackUseCase.execute(any(FeedbackCreateRequest.class), any())).thenReturn(mockResponse);

        String validJsonRequest = """
                {
                    "descricao": "O portfolio ficou excelente e muito responsivo!",
                    "avaliacao": 5,
                    "tipoFeedback": "GERAL",
                    "referenciaId": 0
                }
                """;

        mockMvc.perform(post("/feedback/geral/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Feedback enviado com sucesso!"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar um feedback geral com dados inválidos")
    void deveRetornar400AoCriarFeedbackGeralInvalido() throws Exception {
        mockMvc.perform(post("/feedback/geral/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 200 ao listar todos os feedbacks gerais")
    void deveRetornar200AoListarFeedbacksGerais() throws Exception {
        when(listGeneralFeedbacksUseCase.execute()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/feedback/geral/listar-todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Recursos encontrados"))
                .andExpect(jsonPath("$.dados").isArray());
    }

    @Test
    @DisplayName("Deve retornar 200 ao atualizar um feedback geral com sucesso")
    void deveRetornar200AoAtualizarFeedbackGeral() throws Exception {
        Long idFeedback = 50L;
        FeedbackDTO mockResponse = mock(FeedbackDTO.class);
        when(updateFeedbackUseCase.execute(eq(idFeedback), any(FeedbackCreateRequest.class), any())).thenReturn(mockResponse);

        String validJsonRequest = """
                {
                    "descricao": "Atualizando meu comentario sobre o site inteiro.",
                    "avaliacao": 4,
                    "tipoFeedback": "GERAL",
                    "referenciaId": 0
                }
                """;

        mockMvc.perform(put("/feedback/geral/atualizar/{idFeedback}", idFeedback)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feedback atualizado com sucesso!"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar atualizar um feedback geral com dados inválidos")
    void deveRetornar400AoAtualizarFeedbackGeralInvalido() throws Exception {
        Long idFeedback = 50L;

        mockMvc.perform(put("/feedback/geral/atualizar/{idFeedback}", idFeedback)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 200 ao excluir um feedback geral com sucesso")
    void deveRetornar200AoExcluirFeedbackGeral() throws Exception {
        Long idFeedback = 50L;

        mockMvc.perform(delete("/feedback/geral/excluir/{idFeedback}", idFeedback)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feedback excluído com sucesso!"));

        verify(deleteFeedbackUseCase).execute(eq(idFeedback), any());
    }
}