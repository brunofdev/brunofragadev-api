package com.brunofragadev.module.project.api.controller;

import com.brunofragadev.module.project.api.dto.request.ProjectRequest;
import com.brunofragadev.module.project.api.dto.response.ProjectResponse;
import com.brunofragadev.module.project.application.usecase.*;
import com.brunofragadev.infrastructure.default_return_api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/paineladm/projetos")
@Validated
@SecurityRequirement(name = "bearerAuth") // 👈 Exige o cadeado (Token JWT) para todas as rotas desta classe
@Tag(name = "Painel ADM - Projetos", description = "Endpoints restritos para criação, edição e exclusão de projetos do portfólio")
public class PrivateProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final ListProjectsUseCase listProjectsUseCase;
    private final GetProjectByIdUseCase getProjectByIdUseCase;
    private final UpdateProjectUseCase updateProjectUseCase;
    private final DeleteProjectUseCase deleteProjectUseCase;

    public PrivateProjectController(
            CreateProjectUseCase createProjectUseCase,
            ListProjectsUseCase listProjectsUseCase,
            GetProjectByIdUseCase getProjectByIdUseCase,
            UpdateProjectUseCase updateProjectUseCase,
            DeleteProjectUseCase deleteProjectUseCase) {
        this.createProjectUseCase = createProjectUseCase;
        this.listProjectsUseCase = listProjectsUseCase;
        this.getProjectByIdUseCase = getProjectByIdUseCase;
        this.updateProjectUseCase = updateProjectUseCase;
        this.deleteProjectUseCase = deleteProjectUseCase;
    }

    @PostMapping
    @Operation(summary = "Publicar novo projeto", description = "Cria um novo projeto e o adiciona ao portfólio.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Projeto publicado com sucesso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição")
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(@Valid @RequestBody ProjectRequest request) {
        ProjectResponse createdProject = createProjectUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Projeto publicado com sucesso", createdProject));
    }

    @GetMapping
    @Operation(summary = "Listar todos os projetos (Visão ADM)", description = "Retorna todos os projetos cadastrados no banco de dados para gestão do administrador.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Projetos listados com sucesso")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> listAllProjects() {
        List<ProjectResponse> projects = listProjectsUseCase.execute();
        return ResponseEntity.ok(ApiResponse.success("Projetos listados com sucesso", projects));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar projeto por ID (Visão ADM)", description = "Retorna os detalhes completos de um projeto específico para edição.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Projeto encontrado com sucesso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(
            @Parameter(description = "ID único do projeto") @PathVariable Long id) {
        ProjectResponse project = getProjectByIdUseCase.returnDTO(id);
        return ResponseEntity.ok(ApiResponse.success("Projeto encontrado com sucesso", project));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar projeto", description = "Edita as informações de um projeto já existente no portfólio.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Projeto atualizado com sucesso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @Parameter(description = "ID único do projeto") @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {
        ProjectResponse updatedProject = updateProjectUseCase.execute(id, request);
        return ResponseEntity.ok(ApiResponse.success("Projeto atualizado com sucesso", updatedProject));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir projeto", description = "Remove permanentemente um projeto do portfólio.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Projeto excluído com sucesso (Sem conteúdo de retorno)")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID único do projeto") @PathVariable Long id) {
        deleteProjectUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}