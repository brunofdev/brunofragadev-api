package com.brunofragadev.infrastructure.security;

import com.brunofragadev.infrastructure.default_return_api.ApiError;
import com.brunofragadev.infrastructure.default_return_api.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    /*
     * Para adicionar rate limit a uma nova rota:
     * basta incluir uma nova entrada abaixo com (rota → número máximo de tentativas por minuto).
     */
    private static final Map<String, Integer> ROTAS_LIMITADAS = Map.of(
        "/auth/login",                                   3,
        "/auth/login/google",                            3,
        "/usuario/senha/recuperacao",                    3,
        "/usuario/senha/recuperacao/validar-codigo",     3,
        "/usuario/ativar-conta",                         3,
        "/usuario/reenviar-codigo",                      3
    );

    private static final long JANELA_MS = 60_000;

    private final Map<String, JanelaContador> contadores = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public RateLimitFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String rota = request.getRequestURI();

        if (!ROTAS_LIMITADAS.containsKey(rota)) {
            filterChain.doFilter(request, response);
            return;
        }

        int maxTentativas = ROTAS_LIMITADAS.get(rota);
        String chave = obterIp(request) + ":" + rota;

        JanelaContador janela = contadores.compute(chave, (k, existente) -> {
            long agora = System.currentTimeMillis();
            if (existente == null || agora - existente.inicio > JANELA_MS) {
                return new JanelaContador(agora);
            }
            existente.incrementar();
            return existente;
        });

        if (janela.tentativas > maxTentativas) {
            escreverRespostaErro(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void escreverRespostaErro(HttpServletResponse response) throws IOException {
        ApiError erro = new ApiError();
        erro.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        erro.setError("Too Many Requests");
        erro.setMessage("Limite de tentativas excedido. Aguarde 1 minuto antes de tentar novamente.");
        erro.setTimestamp(LocalDateTime.now());

        ApiResponse<?> apiResponse = ApiResponse.error("Limite de tentativas excedido", erro);

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private String obterIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        return ip.split(",")[0].trim();
    }

    private static class JanelaContador {
        long inicio;
        int tentativas;

        JanelaContador(long inicio) {
            this.inicio = inicio;
            this.tentativas = 1;
        }

        void incrementar() {
            this.tentativas++;
        }
    }
}
