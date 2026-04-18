package com.brunofragadev.module.project.api.controller;

import com.brunofragadev.module.project.api.dto.response.ProjectResponse;
import com.brunofragadev.module.project.application.usecase.GetProjectByIdUseCase;
import com.brunofragadev.module.project.application.usecase.ListProjectsUseCase;
import com.brunofragadev.infrastructure.default_return_api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/projetos/publicos")
@Tag(name = "Portfólio Público", description = "Endpoints abertos para exibição dos projetos no site (Não exige Token)")
public class PublicProjectController {

    private final ListProjectsUseCase listProjectsUseCase;
    private final GetProjectByIdUseCase getProjectByIdUseCase;

    public PublicProjectController(ListProjectsUseCase listProjectsUseCase, GetProjectByIdUseCase getProjectByIdUseCase) {
        this.listProjectsUseCase = listProjectsUseCase;
        this.getProjectByIdUseCase = getProjectByIdUseCase;
    }

    @GetMapping
    @Operation(summary = "Listar projetos da vitrine", description = "Retorna todos os projetos que devem ser exibidos na página inicial do portfólio.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Projetos carregados com sucesso")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> listPublicProjects() {
        List<ProjectResponse> projects = listProjectsUseCase.execute();
        return ResponseEntity.ok(ApiResponse.success("Projetos carregados para o portfólio", projects));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhes do projeto público", description = "Busca as informações completas de um projeto para a tela de detalhes do site.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Detalhes carregados com sucesso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    public ResponseEntity<ApiResponse<ProjectResponse>> getPublicProjectById(
            @Parameter(description = "ID único do projeto") @PathVariable Long id) {
        ProjectResponse project = getProjectByIdUseCase.returnDTO(id);
        return ResponseEntity.ok(ApiResponse.success("Detalhes do projeto carregados", project));
    }
}