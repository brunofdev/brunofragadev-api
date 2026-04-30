package com.brunofragadev.module.user.api.controller;

import com.brunofragadev.module.auth.api.dto.response.UserLoginResponse;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.infrastructure.security.JwtProvider;
import com.brunofragadev.module.user.api.dto.request.UpdateProfileData;
import com.brunofragadev.module.user.api.dto.request.UserRegistrationRequest;
import com.brunofragadev.module.user.api.dto.request.PasswordChangeRequest;
import com.brunofragadev.module.user.api.dto.request.UserValidationRequest;
import com.brunofragadev.module.user.api.dto.response.PasswordRecoveryResponse;
import com.brunofragadev.infrastructure.default_return_api.ApiResponse;
import com.brunofragadev.module.user.application.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Gestão de Usuários", description = "Endpoints para cadastro, recuperação de senha e gestão de perfil")
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
    @Operation(summary = "Cadastrar novo usuário", description = "Cria uma nova conta pendente de ativação via e-mail.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuário criado e aguardando ativação")
    public ResponseEntity<ApiResponse<UserDTO>> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        UserDTO createdUser = registerUserUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.success("Recurso criado", createdUser));
    }

    @GetMapping("/obter-todos")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista com todos os usuários do sistema. Exige token.")
    public ResponseEntity<ApiResponse<List<UserDTO>>> listUsers() {
        return ResponseEntity.ok(ApiResponse.success("Recurso disponivel", listUsersUseCase.execute()));
    }

    @PostMapping("/ativar-conta")
    @Operation(summary = "Ativar conta", description = "Valida o código de 6 dígitos enviado por e-mail e gera o primeiro Token JWT.")
    public ResponseEntity<ApiResponse<UserLoginResponse>> activateAccount(@RequestBody @Valid UserValidationRequest request) {
        UserDTO activatedUser = activateAccountUseCase.execute(request);
        String token = jwtProvider.generateToken(activatedUser.userName(), activatedUser.role());
        return ResponseEntity.ok(ApiResponse.success("Recurso disponivel", new UserLoginResponse(token, activatedUser)));
    }

    @GetMapping("/meus-dados")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Obter dados do perfil", description = "Retorna os dados do usuário atualmente autenticado.")
    public ResponseEntity<ApiResponse<UserDTO>> getAuthenticatedUserData(
            @Parameter(hidden = true) @AuthenticationPrincipal User authenticatedUser) {
        UserDTO userDTO = getAuthenticatedUserUseCase.execute(authenticatedUser.getUsername());
        return ResponseEntity.ok().body(ApiResponse.success("Dados do usuário autenticado", userDTO));
    }

    @PostMapping("/reenviar-codigo")
    @Operation(summary = "Reenviar código de ativação", description = "Gera e envia um novo código de 6 dígitos para o e-mail do usuário.")
    public ResponseEntity<Void> resendCode(@RequestBody String userName) {
        generateVerificationCodeUseCase.execute(userName);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/senha/recuperacao")
    @Operation(summary = "Solicitar recuperação de senha", description = "Envia um e-mail com instruções e código para recuperar a senha esquecida.")
    public ResponseEntity<ApiResponse<PasswordRecoveryResponse>> recoverPassword(@RequestBody String userNameOrEmail) {
        PasswordRecoveryResponse emailDTO = sendPasswordRecoveryEmailUseCase.execute(userNameOrEmail);
        return ResponseEntity.ok(ApiResponse.success("Email enviado com sucesso", emailDTO));
    }

    @PostMapping("/senha/recuperacao/validar-codigo")
    @Operation(summary = "Validar código de recuperação", description = "Verifica se o código de recuperação informado é válido e não expirou.")
    public ResponseEntity<Void> validatePasswordRecoveryCode(@RequestBody UserValidationRequest request) {
        validatePasswordRecoveryCodeUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/senha/recuperacao/alterar-senha")
    @Operation(summary = "Definir nova senha", description = "Altera a senha do usuário após a validação bem-sucedida do código de recuperação.")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid PasswordChangeRequest request) {
        changePasswordUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/meus-dados/atualizar")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar perfil", description = "Modifica os dados cadastrais do usuário autenticado.")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(
            @Parameter(hidden = true) @AuthenticationPrincipal User authenticatedUser,
            @Valid @RequestBody UpdateProfileData request) {
        UserDTO updatedProfile = updateProfileUseCase.execute(authenticatedUser.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Perfil atualizado com sucesso", updatedProfile));
    }
}