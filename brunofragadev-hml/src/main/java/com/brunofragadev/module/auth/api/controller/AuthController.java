package com.brunofragadev.module.auth.api.controller;

import com.brunofragadev.module.auth.api.dto.request.GoogleAuthRequest;
import com.brunofragadev.module.auth.api.dto.response.UserLoginResponse;
import com.brunofragadev.module.auth.api.dto.request.CredentialsRequest;
import com.brunofragadev.infrastructure.default_return_api.ApiResponse;
import com.brunofragadev.module.auth.application.usecase.ApiGoogleAuthUseCase;
import com.brunofragadev.module.auth.application.usecase.AuthUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para login e validação de tokens")
public class AuthController {

    private final AuthUserUseCase authUserUseCase;
    private final ApiGoogleAuthUseCase apiGoogleAuthUseCase;

    public AuthController(AuthUserUseCase authUserUseCase, ApiGoogleAuthUseCase apiGoogleAuthUseCase){
        this.authUserUseCase = authUserUseCase;
        this.apiGoogleAuthUseCase = apiGoogleAuthUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Realizar login clássico", description = "Autentica o usuário com e-mail e senha, retornando o token JWT.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(@Valid @RequestBody CredentialsRequest credentialsRequest) {
        UserLoginResponse loginResult = authUserUseCase.execute(credentialsRequest);
        return ResponseEntity.ok(ApiResponse.success("Usuário autenticado com sucesso", loginResult));
    }

    @PostMapping("/login/google")
    @Operation(summary = "Realizar login com Google", description = "Valida o token do Google OAuth2 e autentica o user no sistema.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login social realizado com sucesso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Token do Google ausente ou inválido")
    public ResponseEntity<ApiResponse<UserLoginResponse>> loginWithGoogle(@Valid @RequestBody GoogleAuthRequest googleAuthRequest) {
        UserLoginResponse loginResult = apiGoogleAuthUseCase.execute(googleAuthRequest);
        return ResponseEntity.ok(ApiResponse.success("Usuário autenticado com sucesso", loginResult));
    }

    @GetMapping("/validar-admin")
    @PreAuthorize("hasRole('ADMIN3')")
    @Operation(summary = "Validar permissão de Administrador", description = "Verifica se o token JWT atual possui a role ADMIN3.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Acesso autorizado")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acesso negado (Não é admin)")
    public ResponseEntity<String> validateAdminAccess() {
        return ResponseEntity.ok("Acesso Autorizado");
    }
}