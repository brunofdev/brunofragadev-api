package com.brunofragadev.security.springsecurity;

import com.brunofragadev.usuarios.Role;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    //ROTAS PUBLICAS
    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/cliente-login",
            "/auth/login",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/usuario/cadastro",
            "/usuario/obter-todos",
            //>>>>>>>>>>>>>LIBERADO PUBLICAMENTE PARA TESTES APENAS<<<<<<<<<<<
    };
    //ROTAS PROTEGIDAS
    private static final Map<String, Role> PROTECTED_ROUTES = Map.of(
            //Apenas USER+
                 "/pedido/criar-pedido", Role.USER
            ,"/pedido/obter-todos-pedidos", Role.USER
            ,"/pedido/obter-pedidos-do-cliente", Role.USER
            //Apenas ADMIN1+
            ,"/produtos/adicionar-produto", Role.ADMIN1
            ,"/pedido/*/status", Role.ADMIN1
            ,"/pedido/obter-pedidos-do-dia", Role.ADMIN1
            ,"/cardapioproduto/associar-cardapioproduto", Role.ADMIN1
            ,"/cardapioproduto/atualizar-campos-custom", Role.ADMIN1
            //Apenas ADMIN2+
            ,"/cardapioproduto/cardapio{idCardapio}/produto{idProduto}", Role.ADMIN2
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