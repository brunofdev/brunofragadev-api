package com.brunofragadev.module.feedback.api.controller;

import com.brunofragadev.infrastructure.log.domain.repository.ErrorLogRepository;
import com.brunofragadev.infrastructure.security.JwtProvider;
import com.brunofragadev.module.auth.application.usecase.AuthorizationService;
import com.brunofragadev.module.feedback.api.dto.request.FeedbackCreateRequest;
import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.feedback.application.usecase.CreateFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.DeleteFeedbackUseCase;
import com.brunofragadev.module.feedback.application.usecase.ListProjectFeedbacksUseCase;
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

@WebMvcTest(ProjectFeedbackController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProjectFeedbackControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CreateFeedbackUseCase createFeedbackUseCase;

    @MockitoBean
    ListProjectFeedbacksUseCase listProjectFeedbacksUseCase;

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
    @DisplayName("Deve retornar 201 ao criar um feedback de projeto com sucesso")
    void deveRetornar201AoCriarFeedbackProjeto() throws Exception {
        FeedbackDTO mockResponse = mock(FeedbackDTO.class);
        when(createFeedbackUseCase.execute(any(FeedbackCreateRequest.class), any())).thenReturn(mockResponse);

        String validJsonRequest = """
                {
                    "descricao": "A arquitetura deste projeto esta incrivel e muito limpa!",
                    "avaliacao": 5,
                    "tipoFeedback": "PROJETO",
                    "referenciaId": 25
                }
                """;

        mockMvc.perform(post("/feedback/projetos/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Feedback enviado com sucesso!"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar um feedback de projeto com dados inválidos")
    void deveRetornar400AoCriarFeedbackProjetoInvalido() throws Exception {
        mockMvc.perform(post("/feedback/projetos/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 200 ao listar feedbacks de um projeto específico")
    void deveRetornar200AoListarFeedbacksDeProjeto() throws Exception {
        Long idProjeto = 25L;
        when(listProjectFeedbacksUseCase.execute(idProjeto)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/feedback/projetos/listar-todos/{idprojeto}", idProjeto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Recursos encontrados"))
                .andExpect(jsonPath("$.dados").isArray());
    }

    @Test
    @DisplayName("Deve retornar 200 ao atualizar um feedback de projeto com sucesso")
    void deveRetornar200AoAtualizarFeedbackProjeto() throws Exception {
        Long idFeedback = 200L;
        FeedbackDTO mockResponse = mock(FeedbackDTO.class);
        when(updateFeedbackUseCase.execute(eq(idFeedback), any(FeedbackCreateRequest.class), any())).thenReturn(mockResponse);

        String validJsonRequest = """
                {
                    "descricao": "Adicionando mais detalhes sobre a minha analise do projeto.",
                    "avaliacao": 4,
                    "tipoFeedback": "PROJETO",
                    "referenciaId": 25
                }
                """;

        mockMvc.perform(put("/feedback/projetos/atualizar/{idFeedback}", idFeedback)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feedback do projeto atualizado com sucesso!"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar atualizar um feedback de projeto com dados inválidos")
    void deveRetornar400AoAtualizarFeedbackProjetoInvalido() throws Exception {
        Long idFeedback = 200L;

        mockMvc.perform(put("/feedback/projetos/atualizar/{idFeedback}", idFeedback)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 200 ao excluir um feedback de projeto com sucesso")
    void deveRetornar200AoExcluirFeedbackProjeto() throws Exception {
        Long idFeedback = 200L;

        mockMvc.perform(delete("/feedback/projetos/excluir/{idFeedback}", idFeedback)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feedback do projeto excluído com sucesso!"));

        verify(deleteFeedbackUseCase).execute(eq(idFeedback), any());
    }
}