package com.brunofragadev.module.project.api.controller;

import com.brunofragadev.module.project.api.dto.response.ProjectResponse;
import com.brunofragadev.module.project.application.usecase.GetProjectByIdUseCase;
import com.brunofragadev.module.project.application.usecase.ListProjectsUseCase;
import com.brunofragadev.infrastructure.default_return_api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/projetos/publicos")
public class PublicProjectController {

    private final ListProjectsUseCase listProjectsUseCase;
    private final GetProjectByIdUseCase getProjectByIdUseCase;

    public PublicProjectController(ListProjectsUseCase listProjectsUseCase, GetProjectByIdUseCase getProjectByIdUseCase) {
        this.listProjectsUseCase = listProjectsUseCase;
        this.getProjectByIdUseCase = getProjectByIdUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> listPublicProjects() {
        List<ProjectResponse> projects = listProjectsUseCase.execute();
        return ResponseEntity.ok(ApiResponse.success("Projetos carregados para o portfólio", projects));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getPublicProjectById(@PathVariable Long id) {
        ProjectResponse project = getProjectByIdUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success("Detalhes do projeto carregados", project));
    }
}