package com.brunofragadev.module.user.api.controller;

import com.brunofragadev.module.auth.api.dto.UserLoginResponse;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.infrastructure.config.JwtProvider;
import com.brunofragadev.module.user.api.dto.request.UpdateProfileData;
import com.brunofragadev.module.user.api.dto.request.UserRegistrationRequest;
import com.brunofragadev.module.user.api.dto.request.PasswordChangeRequest;
import com.brunofragadev.module.user.api.dto.request.UserValidationRequest;
import com.brunofragadev.module.user.api.dto.response.PasswordRecoveryResponse;
import com.brunofragadev.infrastructure.default_return_api.ApiResponse;
import com.brunofragadev.module.user.application.usecase.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/usuario")
@Validated
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final ActivateAccountUseCase activateAccountUseCase;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final GenerateVerificationCodeUseCase generateVerificationCodeUseCase;
    private final SendPasswordRecoveryEmailUseCase sendPasswordRecoveryEmailUseCase;
    private final ValidatePasswordRecoveryCodeUseCase validatePasswordRecoveryCodeUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;
    private final JwtProvider jwtProvider;

    public UserController(
            RegisterUserUseCase registerUserUseCase,
            ListUsersUseCase listUsersUseCase,
            ActivateAccountUseCase activateAccountUseCase,
            GetAuthenticatedUserUseCase getAuthenticatedUserUseCase,
            GenerateVerificationCodeUseCase generateVerificationCodeUseCase,
            SendPasswordRecoveryEmailUseCase sendPasswordRecoveryEmailUseCase,
            ValidatePasswordRecoveryCodeUseCase validatePasswordRecoveryCodeUseCase,
            ChangePasswordUseCase changePasswordUseCase,
            UpdateProfileUseCase updateProfileUseCase,
            JwtProvider jwtProvider) {

        this.registerUserUseCase = registerUserUseCase;
        this.listUsersUseCase = listUsersUseCase;
        this.activateAccountUseCase = activateAccountUseCase;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
        this.generateVerificationCodeUseCase = generateVerificationCodeUseCase;
        this.sendPasswordRecoveryEmailUseCase = sendPasswordRecoveryEmailUseCase;
        this.validatePasswordRecoveryCodeUseCase = validatePasswordRecoveryCodeUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.updateProfileUseCase = updateProfileUseCase;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<ApiResponse<UserDTO>> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        UserDTO createdUser = registerUserUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.success("Recurso criado", createdUser));
    }

    @GetMapping("/obter-todos")
    public ResponseEntity<ApiResponse<List<UserDTO>>> listUsers() {
        return ResponseEntity.ok(ApiResponse.success("Recurso disponivel", listUsersUseCase.execute()));
    }

    @PostMapping("/ativar-conta")
    public ResponseEntity<ApiResponse<UserLoginResponse>> activateAccount(@RequestBody @Valid UserValidationRequest request) {
        UserDTO activatedUser = activateAccountUseCase.execute(request);
        String token = jwtProvider.generateToken(activatedUser.userName(), activatedUser.role());
        return ResponseEntity.ok(ApiResponse.success("Recurso disponivel", new UserLoginResponse(token, activatedUser)));
    }

    @GetMapping("/meus-dados")
    public ResponseEntity<ApiResponse<UserDTO>> getAuthenticatedUserData(@AuthenticationPrincipal User authenticatedUser) {
        UserDTO userDTO = getAuthenticatedUserUseCase.execute(authenticatedUser.getUsername());
        return ResponseEntity.ok().body(ApiResponse.success("Dados do usario autenticado", userDTO));
    }

    @PostMapping("/reenviar-codigo")
    public ResponseEntity<Void> resendCode(@RequestBody String userName) {
        generateVerificationCodeUseCase.execute(userName);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/senha/recuperacao")
    public ResponseEntity<ApiResponse<PasswordRecoveryResponse>> recoverPassword(@RequestBody String userNameOrEmail) {
        PasswordRecoveryResponse emailDTO = sendPasswordRecoveryEmailUseCase.execute(userNameOrEmail);
        return ResponseEntity.ok(ApiResponse.success("Email enviado com sucesso", emailDTO));
    }

    @PostMapping("/senha/recuperacao/validar-codigo")
    public ResponseEntity<Void> validatePasswordRecoveryCode(@RequestBody UserValidationRequest request) {
        validatePasswordRecoveryCodeUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/senha/recuperacao/alterar-senha")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid PasswordChangeRequest request) {
        changePasswordUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/meus-dados/atualizar")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(
            @AuthenticationPrincipal User authenticatedUser,
            @Valid @RequestBody UpdateProfileData request) {
        UserDTO updatedProfile = updateProfileUseCase.execute(authenticatedUser.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Perfil atualizado com sucesso", updatedProfile));
    }
}