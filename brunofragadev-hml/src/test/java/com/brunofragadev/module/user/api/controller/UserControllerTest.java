package com.brunofragadev.module.user.api.controller;

import com.brunofragadev.infrastructure.log.domain.repository.ErrorLogRepository;
import com.brunofragadev.infrastructure.security.JwtProvider;
import com.brunofragadev.module.auth.application.usecase.AuthorizationService;
import com.brunofragadev.module.user.api.dto.request.UpdateProfileData;
import com.brunofragadev.module.user.api.dto.request.UserRegistrationRequest;
import com.brunofragadev.module.user.api.dto.request.UserValidationRequest;
import com.brunofragadev.module.user.api.dto.response.PasswordRecoveryResponse;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.application.usecase.*;
import com.brunofragadev.module.user.domain.entity.User;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UserControllerTest.CustomPrincipalConfig.class)
class UserControllerTest {

    @TestConfiguration
    static class CustomPrincipalConfig implements WebMvcConfigurer {
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new HandlerMethodArgumentResolver() {
                @Override
                public boolean supportsParameter(@NonNull MethodParameter parameter) {
                    return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
                }

                @Override
                public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                              @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                    User mockUser = mock(User.class);
                    when(mockUser.getUsername()).thenReturn("brunofdev");
                    return mockUser;
                }
            });
        }
    }

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    ListUsersUseCase listUsersUseCase;

    @MockitoBean
    ActivateAccountUseCase activateAccountUseCase;

    @MockitoBean
    GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    @MockitoBean
    GenerateVerificationCodeUseCase generateVerificationCodeUseCase;

    @MockitoBean
    SendPasswordRecoveryEmailUseCase sendPasswordRecoveryEmailUseCase;

    @MockitoBean
    ValidatePasswordRecoveryCodeUseCase validatePasswordRecoveryCodeUseCase;

    @MockitoBean
    ChangePasswordUseCase changePasswordUseCase;

    @MockitoBean
    UpdateProfileUseCase updateProfileUseCase;

    @MockitoBean
    JwtProvider jwtProvider;

    @MockitoBean
    ErrorLogRepository errorLogRepository;

    @MockitoBean
    AuthorizationService authorizationService;

    @Test
    @DisplayName("Deve retornar 200 ao cadastrar novo usuario")
    void deveRetornar200AoCadastrarUsuario() throws Exception {
        UserDTO mockResponse = mock(UserDTO.class);
        when(registerUserUseCase.execute(any(UserRegistrationRequest.class))).thenReturn(mockResponse);

        String validJsonRequest = """
                {
                    "nome": "Bruno de Fraga",
                    "email": "bruno@email.com",
                    "userName": "brunofdev",
                    "senha": "Password@123"
                }
                """;

        mockMvc.perform(post("/usuario/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Recurso criado"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar cadastrar usuario com dados invalidos")
    void deveRetornar400AoCadastrarUsuarioInvalido() throws Exception {
        mockMvc.perform(post("/usuario/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 200 ao listar todos os usuarios")
    void deveRetornar200AoListarUsuarios() throws Exception {
        when(listUsersUseCase.execute()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/usuario/obter-todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Recurso disponivel"));
    }

    @Test
    @DisplayName("Deve retornar 200 ao ativar conta com sucesso")
    void deveRetornar200AoAtivarConta() throws Exception {
        UserDTO mockUserDTO = mock(UserDTO.class);
        when(mockUserDTO.userName()).thenReturn("brunofdev");
        when(activateAccountUseCase.execute(any(UserValidationRequest.class))).thenReturn(mockUserDTO);
        when(jwtProvider.generateToken(any(), any())).thenReturn("token-falso");

        String validJsonRequest = """
                {
                    "userName": "brunofdev",
                    "codigo": "123456"
                }
                """;

        mockMvc.perform(post("/usuario/ativar-conta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Recurso disponivel"))
                .andExpect(jsonPath("$.dados.token").value("token-falso"));
    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar dados do usuario autenticado")
    void deveRetornar200AoBuscarDadosAutenticados() throws Exception {
        UserDTO mockResponse = mock(UserDTO.class);
        when(getAuthenticatedUserUseCase.execute("brunofdev")).thenReturn(mockResponse);

        mockMvc.perform(get("/usuario/meus-dados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Dados do usuário autenticado"));
    }

    @Test
    @DisplayName("Deve retornar 204 ao reenviar codigo")
    void deveRetornar204AoReenviarCodigo() throws Exception {
        mockMvc.perform(post("/usuario/reenviar-codigo")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("brunofdev"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 200 ao solicitar recuperacao de senha")
    void deveRetornar200AoSolicitarRecuperacaoSenha() throws Exception {
        PasswordRecoveryResponse mockResponse = mock(PasswordRecoveryResponse.class);
        when(sendPasswordRecoveryEmailUseCase.execute(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/usuario/senha/recuperacao")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("bruno@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Email enviado com sucesso"));
    }

    @Test
    @DisplayName("Deve retornar 204 ao validar codigo de recuperacao")
    void deveRetornar204AoValidarCodigoRecuperacao() throws Exception {
        String validJsonRequest = """
                {
                    "userName": "bruno@email.com",
                    "codigo": "123456"
                }
                """;

        mockMvc.perform(post("/usuario/senha/recuperacao/validar-codigo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 204 ao alterar senha")
    void deveRetornar204AoAlterarSenha() throws Exception {
        String validJsonRequest = """
                {
                    "userName": "bruno@email.com",
                    "novaSenha": "NewPassword@123",
                    "codigoVerificado": "123456"
                }
                """;

        mockMvc.perform(post("/usuario/senha/recuperacao/alterar-senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 200 ao atualizar o perfil do usuario logado")
    void deveRetornar200AoAtualizarPerfil() throws Exception {
        UserDTO mockResponse = mock(UserDTO.class);
        when(updateProfileUseCase.execute(eq("brunofdev"), any(UpdateProfileData.class))).thenReturn(mockResponse);

        String validJsonRequest = """
                {
                    "nomePublico": "Bruno Dev",
                    "profissao": "Desenvolvedor Backend",
                    "pais": "Brasil"
                }
                """;

        mockMvc.perform(patch("/usuario/meus-dados/atualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Perfil atualizado com sucesso"));
    }
}