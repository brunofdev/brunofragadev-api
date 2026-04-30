package com.brunofragadev.module.auth.api.controller;

// Imports de Domínio e Infraestrutura do seu Projeto
import com.brunofragadev.infrastructure.log.domain.repository.ErrorLogRepository;
import com.brunofragadev.infrastructure.security.JwtProvider;
import com.brunofragadev.module.auth.api.dto.request.CredentialsRequest;
import com.brunofragadev.module.auth.api.dto.request.GoogleAuthRequest;
import com.brunofragadev.module.auth.api.dto.response.UserLoginResponse;
import com.brunofragadev.module.auth.application.usecase.ApiGoogleAuthUseCase;
import com.brunofragadev.module.auth.application.usecase.AuthUserUseCase;
import com.brunofragadev.module.auth.application.usecase.AuthorizationService;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.domain.entity.UserDTOBuilder;

// Imports de Ferramentas e Framework
import com.brunofragadev.module.user.domain.exception.InvalidCredentialsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// Static Imports para MockMvc (Caminhos Corretos)
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Static Imports para Mockito e Assertions
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AuthUserUseCase authUserUseCase;

    @MockitoBean
    ApiGoogleAuthUseCase apiGoogleAuthUseCase;

    @MockitoBean
    ErrorLogRepository errorLogRepository;

    @MockitoBean
    AuthorizationService authorizationService;

    @MockitoBean
    JwtProvider jwtProvider;

    @Test
    @DisplayName("Deve autenticar usuário com sucesso quando as credenciais clássicas forem válidas")
    void deveRetornar200QuandoLoginForValido() throws Exception {
        CredentialsRequest request = new CredentialsRequest("ellok", "1234567");
        UserDTO user = UserDTOBuilder.umUsuario().build();
        UserLoginResponse response = new UserLoginResponse("jwt-token-falso", user);

        when(authUserUseCase.execute(request)).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuário autenticado com sucesso"))
                .andExpect(jsonPath("$.dados.token").value("jwt-token-falso"));
    }

    @Test
    @DisplayName("Deve autenticar usuário via Google quando o token social for válido")
    void deveRetornar200QuandoLoginGoogleForValido() throws Exception {
        GoogleAuthRequest request = new GoogleAuthRequest("google-token-xyz");
        UserDTO user = UserDTOBuilder.umUsuario().build();
        UserLoginResponse response = new UserLoginResponse("jwt-google-falso", user);

        when(apiGoogleAuthUseCase.execute(any(GoogleAuthRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login/google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuário autenticado com sucesso"))
                .andExpect(jsonPath("$.dados.token").value("jwt-google-falso"));
    }

    @Test
    @DisplayName("Deve retornar 200 no endpoint de validação de admin (sem filtros de segurança ativos)")
    void deveRetornar200AoValidarAdmin() throws Exception {
        mockMvc.perform(get("/auth/validar-admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Acesso Autorizado"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 (Bad Request) quando o corpo da requisição estiver incompleto")
    void deveRetornar400QuandoCorpoDaRequisicaoForInvalido() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro 401 (Unauthorized) quando o serviço de autenticação lançar erro de credenciais")
    void deveRetornar401QuandoCredenciaisForemInvalidas() throws Exception {
        CredentialsRequest request = new CredentialsRequest("usuario_errado", "senha_errada");

        when(authUserUseCase.execute(any())).thenThrow(new InvalidCredentialsException("Credenciais inválidas"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
