package com.brunofragadev.module.project.api.controller;

import com.brunofragadev.infrastructure.log.domain.repository.ErrorLogRepository;
import com.brunofragadev.infrastructure.security.JwtProvider;
import com.brunofragadev.module.auth.application.usecase.AuthorizationService;
import com.brunofragadev.module.project.api.dto.request.ProjectRequest;
import com.brunofragadev.module.project.api.dto.response.ProjectResponse;
import com.brunofragadev.module.project.application.usecase.*;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrivateProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
class PrivateProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CreateProjectUseCase createProjectUseCase;

    @MockitoBean
    ListProjectsUseCase listProjectsUseCase;

    @MockitoBean
    GetProjectByIdUseCase getProjectByIdUseCase;

    @MockitoBean
    UpdateProjectUseCase updateProjectUseCase;

    @MockitoBean
    DeleteProjectUseCase deleteProjectUseCase;

    @MockitoBean
    ErrorLogRepository errorLogRepository;

    @MockitoBean
    AuthorizationService authorizationService;

    @MockitoBean
    JwtProvider jwtProvider;

    @Test
    @DisplayName("Deve retornar 201 ao publicar um novo projeto com sucesso")
    void deveRetornar201AoCriarProjeto() throws Exception {
        ProjectResponse mockResponse = mock(ProjectResponse.class);
        when(createProjectUseCase.execute(any(ProjectRequest.class))).thenReturn(mockResponse);

        String validJsonRequest = """
                {
                    "title": "API de Gestão de Restaurantes",
                    "description": "Sistema desenvolvido para controlar pedidos",
                    "status": "Concluído",
                    "papel": "Desenvolvedor Backend"
                }
                """;

        mockMvc.perform(post("/paineladm/projetos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Projeto publicado com sucesso"));
    }

    @Test
    @DisplayName("Deve retornar 200 ao listar todos os projetos")
    void deveRetornar200AoListarProjetos() throws Exception {
        when(listProjectsUseCase.execute()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/paineladm/projetos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Projetos listados com sucesso"))
                .andExpect(jsonPath("$.dados").isArray());
    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar um projeto por ID existente")
    void deveRetornar200AoBuscarProjetoPorId() throws Exception {
        Long id = 1L;
        ProjectResponse mockResponse = mock(ProjectResponse.class);
        when(getProjectByIdUseCase.returnDTO(id)).thenReturn(mockResponse);

        mockMvc.perform(get("/paineladm/projetos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Projeto encontrado com sucesso"));
    }

    @Test
    @DisplayName("Deve retornar 200 ao atualizar um projeto existente")
    void deveRetornar200AoAtualizarProjeto() throws Exception {
        Long id = 1L;
        ProjectResponse mockResponse = mock(ProjectResponse.class);
        when(updateProjectUseCase.execute(eq(id), any(ProjectRequest.class))).thenReturn(mockResponse);

        String validJsonRequest = """
                {
                    "title": "API de Gestão Atualizada",
                    "description": "Nova descrição do sistema",
                    "status": "Em andamento",
                    "papel": "Arquiteto de Software"
                }
                """;

        mockMvc.perform(put("/paineladm/projetos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Projeto atualizado com sucesso"));
    }

    @Test
    @DisplayName("Deve retornar 204 ao excluir um projeto com sucesso")
    void deveRetornar204AoExcluirProjeto() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/paineladm/projetos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    @Test
    @DisplayName("Deve retornar 404 ao tentar buscar um projeto com ID inexistente")
    void deveRetornar404AoBuscarProjetoInexistente() throws Exception {
        Long idInexistente = 999L;
        // Assumindo que você tem uma exceção específica para isso tratada no GlobalExceptionHandler
        when(getProjectByIdUseCase.returnDTO(idInexistente))
                .thenThrow(new ProjectNotFoundException("Projeto não encontrado"));

        mockMvc.perform(get("/paineladm/projetos/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar um projeto com ID inexistente")
    void deveRetornar404AoAtualizarProjetoInexistente() throws Exception {
        Long idInexistente = 999L;
        when(updateProjectUseCase.execute(eq(idInexistente), any(ProjectRequest.class)))
                .thenThrow(new ProjectNotFoundException("Projeto não encontrado"));

        String validJsonRequest = """
                {
                    "title": "API de Gestão Atualizada",
                    "status": "Em andamento"
                }
                """;

        mockMvc.perform(put("/paineladm/projetos/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar excluir um projeto com ID inexistente")
    void deveRetornar404AoExcluirProjetoInexistente() throws Exception {
        Long idInexistente = 999L;

        doThrow(new ProjectNotFoundException("Projeto não encontrado"))
                .when(deleteProjectUseCase).execute(idInexistente);

        mockMvc.perform(delete("/paineladm/projetos/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}