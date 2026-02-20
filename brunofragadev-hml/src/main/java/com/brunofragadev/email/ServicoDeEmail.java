package com.brunofragadev.email;

import com.brunofragadev.feedback.FeedbackDTO;
import com.brunofragadev.usuarios.UsuarioDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class ServicoDeEmail {

    private final WebClient clienteWeb;

    @Value("${brevo.api.key}")
    private String chaveApi;

    @Value("${brevo.sender.email}")
    private String emailRemetente;

    public ServicoDeEmail(WebClient.Builder construtorWebClient) {
        // Inicializa o WebClient UMA UNICA VEZ no construtor
        this.clienteWeb = construtorWebClient.baseUrl("https://api.brevo.com/v3").build();
    }

    /**
     * MOTOR CENTRAL: Metodo privado que faz o trabalho de falar com a API do Brevo.
     * Assim não repetimos o codigo de requisição em todos os emails.
     */
    private void enviarEmailPelaApi(String emailDestino, String nomeDestino, String assunto, String conteudoHtml) {
        Map<String, Object> corpoRequisicao = Map.of(
                "sender", Map.of("email", emailRemetente, "name", "Bruno Fraga Dev"),
                "to", List.of(Map.of("email", emailDestino, "name", nomeDestino)),
                "subject", assunto,
                "htmlContent", conteudoHtml
        );

        clienteWeb.post()
                .uri("/smtp/email")
                .header("api-key", chaveApi)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(corpoRequisicao)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    // =========================================================================
    // 1. CÓDIGO DE VERIFICAÇÃO (Cadastro Novo)
    // =========================================================================
    public void enviarEmailDeVerificacao(String emailDestino, String nomeDestino, String codigoVerificacao) {
        String assunto = "Código de Verificação - Bruno Dev";
        String conteudoHtml = String.format("""
                <html>
                <body style="font-family: Arial, sans-serif; color: #333;">
                    <h2>Olá %s,</h2>
                    <p>Obrigado por se cadastrar no meu portfólio! Para liberar seu acesso, digite o código abaixo:</p>
                    <div style="background-color: #181818; padding: 20px; border-radius: 8px; text-align: center; margin: 20px 0;">
                        <h1 style="color: #4caf50; letter-spacing: 10px; margin: 0; font-size: 36px;">%s</h1>
                    </div>
                    <p style="color: #f50505; font-size: 12px;"><strong>Aviso:</strong> Este código expira em 15 minutos.</p>
                </body>
                </html>
                """, nomeDestino, codigoVerificacao);

        enviarEmailPelaApi(emailDestino, nomeDestino, assunto, conteudoHtml);
        System.out.println("✅ E-mail de verificação enviado para: " + emailDestino);
    }

    // =========================================================================
    // 2. BOAS-VINDAS (Após a verificação do código)
    // =========================================================================
    public void enviarEmailDeBoasVindas(UsuarioDTO usuario) {
        String assunto = "Seja bem-vindo(a), " + usuario.nome() + "!";
        String conteudoHtml = String.format("<html><body><h1>Olá %s!</h1><p>Sua conta foi verificada com sucesso. Estamos felizes em ter você conosco!</p></body></html>", usuario.nome());

        enviarEmailPelaApi(usuario.email(), usuario.nome(), assunto, conteudoHtml);
        System.out.println("✅ E-mail de boas-vindas enviado para: " + usuario.email());
    }

    // =========================================================================
    // 3. ALERTA PARA O ADMIN (Quando alguém envia um feedback)
    // =========================================================================
    public void enviarAlertaDeNovoFeedbackParaAdmin(FeedbackDTO feedback) {
        String assunto = "Novo Feedback Recebido no seu Portfólio!";
        String conteudoHtml = String.format("""
                <html><body>
                    <h2>Olá Bruno, você recebeu um novo feedback!</h2>
                    <p><strong>De:</strong> %s</p>
                    <p><strong>Nota:</strong> %d de 5 estrelas</p>
                    <p><strong>Comentário:</strong></p>
                    <blockquote style="border-left: 2px solid #ccc; padding-left: 10px; margin-left: 5px;">
                        %s
                    </blockquote>
                </body></html>
                """, feedback.criadoPor(), feedback.notaAvaliacao(), feedback.comentario());

        // Envia sempre para o e-mail do dono do portfólio
        enviarEmailPelaApi("brunofragaa97@gmail.com", "Bruno Fraga (Admin)", assunto, conteudoHtml);
    }

    // =========================================================================
    // 4. AGRADECIMENTO AO USUÁRIO (Após ele deixar o feedback)
    // =========================================================================
    public void enviarAgradecimentoDeFeedbackParaUsuario(UsuarioDTO usuario) {
        String assunto = "Feedback postado — obrigado!";
        String conteudoHtml = String.format("""
                <html><body>
                    <h2>Olá %s,</h2>
                    <p>Você acabou de deixar um novo feedback para <strong>Bruno Fraga Dev</strong>.</p>
                    <p><strong>Isso faz toda a diferença e me ajuda a evoluir!</strong></p>
                    <p>Continue contribuindo e ajudando outras pessoas a se desenvolverem. 💪</p>
                </body></html>
                """, usuario.nome());

        enviarEmailPelaApi(usuario.email(), usuario.nome(), assunto, conteudoHtml);
        System.out.printf("✅ E-mail de agradecimento enviado para %s%n", usuario.nome());
    }
}