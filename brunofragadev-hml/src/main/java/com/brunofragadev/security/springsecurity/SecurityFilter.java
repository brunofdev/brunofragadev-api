package com.brunofragadev.security.springsecurity;


import com.brunofragadev.configs.JwtProvider;
import com.brunofragadev.autenticacao.service.AuthorizationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthorizationService authorizationService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException{
        var token = this.recoverToken(request);

        if(token != null){
            var login = jwtProvider.validateToken(token);

            if(login != null){
                UserDetails usuario = authorizationService.loadUserByUsername(login);
                var autenticacao = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(autenticacao);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        // 1. Pega o cabeçalho "Authorization" da requisição
        var authHeader = request.getHeader("Authorization");

        // 2. Se não tiver cabeçalho, retorna nulo (não tem token)
        if (authHeader == null) return null;

        // 3. Substitui "Bearer " por nada (vazio), sobrando só o token
        return authHeader.replace("Bearer ", "");
    }
}
