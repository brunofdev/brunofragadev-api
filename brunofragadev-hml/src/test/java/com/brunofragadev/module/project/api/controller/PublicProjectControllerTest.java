package com.brunofragadev.module.project.api.controller;

import com.brunofragadev.infrastructure.log.domain.repository.ErrorLogRepository;
import com.brunofragadev.infrastructure.security.JwtProvider;
import com.brunofragadev.module.auth.application.usecase.AuthorizationService;
import com.brunofragadev.module.project.api.dto.response.ProjectResponse;
import com.brunofragadev.module.project.application.usecase.GetProjectByIdUseCase;
import com.brunofragadev.module.project.application.usecase.ListProjectsUseCase;
import com.brunofragadev.module.project.domain.exception.ProjectNotFoundException;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
class PublicProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ListProjectsUseCase listProjectsUseCase;

    @MockitoBean
    GetProjectByIdUseCase getProjectByIdUseCase;

    @MockitoBean
    ErrorLogRepository errorLogRepository;

    @MockitoBean
    AuthorizationService authorizationService;

    @MockitoBean
    JwtProvider jwtProvider;

    @Test
    @DisplayName("Deve retornar 200 ao listar projetos da vitrine")
    void deveRetornar200AoListarProjetosPublicos() throws Exception {
        when(listProjectsUseCase.execute()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/projetos/publicos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Projetos carregados para o portfólio"))
                .andExpect(jsonPath("$.dados").isArray());
    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar detalhes de um projeto publico existente")
    void deveRetornar200AoBuscarProjetoPublicoPorId() throws Exception {
        Long id = 1L;
        ProjectResponse mockResponse = mock(ProjectResponse.class);
        when(getProjectByIdUseCase.returnDTO(id)).thenReturn(mockResponse);

        mockMvc.perform(get("/projetos/publicos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Detalhes do projeto carregados"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar detalhes de um projeto publico inexistente")
    void deveRetornar404AoBuscarProjetoPublicoInexistente() throws Exception {
        Long idInexistente = 999L;
        when(getProjectByIdUseCase.returnDTO(idInexistente))
                .thenThrow(new ProjectNotFoundException("Projeto não encontrado"));

        mockMvc.perform(get("/projetos/publicos/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}