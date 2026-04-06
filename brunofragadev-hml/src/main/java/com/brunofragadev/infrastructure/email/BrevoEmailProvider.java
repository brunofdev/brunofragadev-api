package com.brunofragadev.infrastructure.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class BrevoEmailProvider implements EmailProvider {

    private final WebClient webClient;

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    public BrevoEmailProvider(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.brevo.com/v3").build();
    }

    @Override
    public void send(String para, String nomeDestinatario, String assunto, String conteudoHtml) {
        Map<String, Object> requestBody = Map.of(
                "sender", Map.of("email", senderEmail, "name", "Bruno Fraga Dev"),
                "to", List.of(Map.of("email", para, "name", nomeDestinatario)),
                "subject", assunto,
                "htmlContent", conteudoHtml
        );

        webClient.post()
                .uri("/smtp/email")
                .header("api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .toBodilessEntity()
                .block();

        System.out.println("E-mail enviado via Brevo para: " + para);
    }
}
