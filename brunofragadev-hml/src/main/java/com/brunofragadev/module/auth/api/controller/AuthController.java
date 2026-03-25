package com.brunofragadev.module.auth.api.controller;

import com.brunofragadev.module.auth.api.dto.GoogleAuthRequest;
import com.brunofragadev.module.auth.api.dto.UserLoginResponse;
import com.brunofragadev.module.auth.api.dto.CredentialsRequest;
import com.brunofragadev.infrastructure.default_return_api.ApiResponse;
import com.brunofragadev.module.auth.application.usecase.ApiGoogleAuthUseCase;
import com.brunofragadev.module.auth.application.usecase.AuthUserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserUseCase authUserUseCase;
    private final ApiGoogleAuthUseCase apiGoogleAuthUseCase;

    public AuthController(AuthUserUseCase authUserUseCase, ApiGoogleAuthUseCase apiGoogleAuthUseCase){
        this.authUserUseCase = authUserUseCase;
        this.apiGoogleAuthUseCase = apiGoogleAuthUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(@Valid @RequestBody CredentialsRequest credentialsRequest) {
        UserLoginResponse loginResult = authUserUseCase.execute(credentialsRequest);
        return ResponseEntity.ok(ApiResponse.success("Usuário autenticado com sucesso", loginResult));
    }

    @PostMapping("/login/google")
    public ResponseEntity<ApiResponse<UserLoginResponse>> loginWithGoogle(@Valid @RequestBody GoogleAuthRequest googleAuthRequest) {
        UserLoginResponse loginResult = apiGoogleAuthUseCase.execute(googleAuthRequest);
        return ResponseEntity.ok(ApiResponse.success("Usuário autenticado com sucesso", loginResult));
    }

    @GetMapping("/validar-admin")
    @PreAuthorize("hasRole('ADMIN3')")
    public ResponseEntity<String> validateAdminAccess() {
        return ResponseEntity.ok("Acesso Autorizado");
    }
}