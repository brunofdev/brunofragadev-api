package com.brunofragadev.infrastructure.config;

import com.brunofragadev.module.user.domain.entity.Role;
import com.brunofragadev.infrastructure.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static java.util.Map.entry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    //ROTAS PUBLICAS
    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/cliente-login",
            "/auth/login",
            "/auth/login/google",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/usuario/cadastro",
            "/usuario/obter-todos",
            "/usuario/ativar-conta",
            "/usuario/reenviar-codigo",
            "/usuario/senha/recuperacao",
            "/usuario/senha/recuperacao/validar-codigo",
            "/usuario/senha/recuperacao/alterar-senha",
            "/projetos/publicos",
            "/feedback/geral/listar-todos",
            "/feedback/projetos/listar-todos/**",
            "/feedback/artigos/listar-todos/**",
            "/geral/artigos/listar-todos",
            "/geral/artigos/**",
            "/geral/artigos/{slug}"


            //>>>>>>>>>>>>>LIBERADO PUBLICAMENTE PARA TESTES APENAS<<<<<<<<<<<
    };
    //ROTAS PROTEGIDAS
    private static final Map<String, Role> PROTECTED_ROUTES = Map.ofEntries(
            // =========================================
            // USUÁRIO COMUM (Apenas USER ou superior)
            // =========================================
            entry("/usuario/meus-dados", Role.USER),
            entry("/usuario/meus-dados/atualizar", Role.USER),

            // =========================================
            // FEEDBACKS (Geral e Projetos)
            // Libera a leitura (GET), mas protege as ações
            // =========================================
            entry("/feedback/geral/criar", Role.USER),
            entry("/feedback/geral/atualizar/**", Role.USER),
            entry("/feedback/geral/excluir/**", Role.USER),

            entry("/feedback/projetos/criar", Role.USER),
            entry("/feedback/projetos/atualizar/**", Role.USER),
            entry("/feedback/projetos/excluir/**", Role.USER),

            entry("/feedback/artigos/criar", Role.USER),
            entry("/feedback/artigos/atualizar/**", Role.USER),
            entry("/feedback/artigos/excluir/**", Role.USER),

            // =========================================
            // ADMINISTRAÇÃO (Apenas ADMIN3)
            // =========================================
            entry("/paineladm/projetos", Role.ADMIN3),
            entry("/paineladm/projetos/**", Role.ADMIN3),
            entry("/paineladm/artigos/**", Role.ADMIN3),
            entry("/paineladm/atualizar/{id}", Role.ADMIN3)
    );

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(PUBLIC_ENDPOINTS).permitAll();
                    PROTECTED_ROUTES.forEach((url, role) -> {
                        authorize.requestMatchers(url).hasRole(role.name());
                    });
                    authorize.anyRequest().hasRole("ADMIN3");
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}