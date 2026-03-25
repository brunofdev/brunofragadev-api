package com.brunofragadev.module.project.api.controller;

import com.brunofragadev.module.project.api.dto.request.ProjectRequest;
import com.brunofragadev.module.project.api.dto.response.ProjectResponse;
import com.brunofragadev.module.project.application.usecase.*;
import com.brunofragadev.infrastructure.default_return_api.ApiResponse;
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
@Tag(name = "1. Gerenciamento de Projetos", description = "Endpoints restritos para criação e edição de projetos (Exige Token)")
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
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(@Valid @RequestBody ProjectRequest request) {
        ProjectResponse createdProject = createProjectUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Projeto publicado com sucesso", createdProject));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> listAllProjects() {
        List<ProjectResponse> projects = listProjectsUseCase.execute();
        return ResponseEntity.ok(ApiResponse.success("Projetos listados com sucesso", projects));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable Long id) {
        ProjectResponse project = getProjectByIdUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success("Projeto encontrado com sucesso", project));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {
        ProjectResponse updatedProject = updateProjectUseCase.execute(id, request);
        return ResponseEntity.ok(ApiResponse.success("Projeto atualizado com sucesso", updatedProject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        deleteProjectUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}