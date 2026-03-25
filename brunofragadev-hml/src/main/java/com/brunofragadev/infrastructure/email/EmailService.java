package com.brunofragadev.infrastructure.email;

import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private final WebClient webClient;

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    public EmailService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.brevo.com/v3").build();
    }

    private void sendEmailViaApi(String targetEmail, String targetName, String subject, String htmlContent) {
        Map<String, Object> requestBody = Map.of(
                "sender", Map.of("email", senderEmail, "name", "Bruno Fraga Dev"),
                "to", List.of(Map.of("email", targetEmail, "name", targetName)),
                "subject", subject,
                "htmlContent", htmlContent
        );

        webClient.post()
                .uri("/smtp/email")
                .header("api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    // =========================================================================
    // 1. CÓDIGO DE VERIFICAÇÃO (Cadastro Novo)
    // =========================================================================
    public void sendVerificationEmail(String targetEmail, String targetName, String verificationCode) {
        String subject = "Código de Verificação - Bruno Dev";
        String htmlContent = String.format("""
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
                """, targetName, verificationCode);

        sendEmailViaApi(targetEmail, targetName, subject, htmlContent);
        System.out.println("✅ E-mail de verificação enviado para: " + targetEmail);
    }

    // =========================================================================
    // 2. BOAS-VINDAS (Após a verificação do código)
    // =========================================================================
    public void sendWelcomeEmail(UserDTO user) {
        String subject = "Seja bem-vindo(a), " + user.nome() + "!";
        String htmlContent = String.format("<html><body><h1>Olá %s!</h1><p>Sua conta foi verificada com sucesso. Estamos felizes em ter você conosco!</p></body></html>", user.nome());

        sendEmailViaApi(user.email(), user.nome(), subject, htmlContent);
        System.out.println("✅ E-mail de boas-vindas enviado para: " + user.email());
    }

    // =========================================================================
    // 3. ALERTA PARA O ADMIN (Quando alguém envia um feedback)
    // =========================================================================
    public void sendNewFeedbackAlertToAdmin(FeedbackDTO feedback, String feedbackLocation) {
        String subject = "Novo Feedback Recebido no seu Portfólio!";
        String htmlContent = String.format("""
                <html><body>
                    <h2>Olá Bruno, você recebeu um novo feedback!</h2>
                    <p><strong>De:</strong> %s</p>
                    <p><strong>Nota:</strong> %d de 5 estrelas</p>
                    <p><strong>Comentário:</strong></p>
                    <blockquote style="border-left: 2px solid #ccc; padding-left: 10px; margin-left: 5px;">
                        %s
                    </blockquote>
                    <p><strong>Local de postagem:</strong> %s</p>
                </body></html>
                """, feedback.criadoPor(), feedback.notaAvaliacao(), feedback.comentario(), feedbackLocation);

        sendEmailViaApi("brunofragaa97@gmail.com", "Bruno Fraga (Admin)", subject, htmlContent);
    }

    // =========================================================================
    // 4. AGRADECIMENTO AO USUÁRIO (Após ele deixar o feedback)
    // =========================================================================
    public void sendFeedbackThankYouEmail(UserDTO user) {
        String subject = "Feedback postado — obrigado!";
        String htmlContent = String.format("""
                <html><body>
                    <h2>Olá %s,</h2>
                    <p>Você acabou de deixar um novo feedback para <strong>Bruno Fraga Dev</strong>.</p>
                    <p><strong>Isso faz toda a diferença e me ajuda a evoluir!</strong></p>
                    <p>Continue contribuindo e ajudando outras pessoas a se desenvolverem. 💪</p>
                </body></html>
                """, user.nome());

        sendEmailViaApi(user.email(), user.nome(), subject, htmlContent);
        System.out.printf("✅ E-mail de agradecimento enviado para %s%n", user.nome());
    }

    // =========================================================================
    // 5. RECUPERAÇÃO DE SENHA (Código de 6 dígitos)
    // =========================================================================
    public void sendPasswordRecoveryEmail(String targetEmail, String targetName, String verificationCode) {
        String subject = "Recuperação de Senha - Bruno Dev";
        String htmlContent = String.format("""
                <html>
                <body style="font-family: Arial, sans-serif; color: #333;">
                    <h2>Olá %s,</h2>
                    <p>Recebemos um pedido para redefinir a senha da sua conta.</p>
                    <p>Use o código de segurança abaixo no sistema para criar uma nova senha:</p>
                    <div style="background-color: #181818; padding: 20px; border-radius: 8px; text-align: center; margin: 20px 0;">
                        <h1 style="color: #4caf50; letter-spacing: 10px; margin: 0; font-size: 36px;">%s</h1>
                    </div>
                    <p style="color: #f50505; font-size: 12px;">
                        <strong>Aviso:</strong> Este código expira em 5 minutos. Se você não pediu para alterar sua senha, ignore este e-mail. Sua conta está segura.
                    </p>
                </body>
                </html>
                """, targetName, verificationCode);

        sendEmailViaApi(targetEmail, targetName, subject, htmlContent);
        System.out.println("✅ E-mail de recuperação de senha enviado para: " + targetEmail);
    }

    // =========================================================================
    // 6. LEMBRETE DE NOME DE USUÁRIO (Apenas o UserName)
    // =========================================================================
    public void sendUsernameReminderEmail(String targetEmail, String targetName, String userName) {
        String subject = "Seu Nome de Usuário - Bruno Dev";
        String htmlContent = String.format("""
                <html>
                <body style="font-family: Arial, sans-serif; color: #333;">
                    <h2>Olá %s,</h2>
                    <p>Você solicitou um lembrete do seu acesso ao nosso sistema.</p>
                    <p>O seu nome de usuário (login) é:</p>
                    <div style="background-color: #f4f4f4; padding: 15px; border-radius: 5px; margin: 15px 0; border-left: 4px solid #4caf50;">
                        <h2 style="color: #1a1a1a; margin: 0; letter-spacing: 2px;">%s</h2>
                    </div>
                    <p style="font-size: 14px; color: #555;">
                        Agora você pode voltar ao site e fazer o login. Se também esqueceu a senha, clique em "Esqueci minha senha" e informe este nome de usuário.
                    </p>
                </body>
                </html>
                """, targetName, userName);

        sendEmailViaApi(targetEmail, targetName, subject, htmlContent);
        System.out.println("✅ E-mail de lembrete de usuário enviado para: " + targetEmail);
    }

    // =========================================================================
    // 7. CONFIRMAÇÃO DE ALTERAÇÃO DE SENHA (Aviso de Segurança)
    // =========================================================================
    public void sendPasswordChangedSuccessfullyEmail(String targetEmail, String targetName) {
        String subject = "Alerta de Segurança: Sua senha foi alterada";

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm:ss");
        String currentDateTime = java.time.LocalDateTime.now().format(formatter);

        String htmlContent = String.format("""
                <html>
                <body style="font-family: Arial, sans-serif; color: #333;">
                    <h2>Olá %s,</h2>
                    <p>A senha da sua conta no portfólio Bruno Dev acabou de ser alterada.</p>
                    
                    <div style="background-color: #f9f9f9; padding: 20px; border-radius: 8px; border-left: 5px solid #48bb78; margin: 20px 0;">
                        <h3 style="margin-top: 0; color: #1a1a1a;">Detalhes da Alteração</h3>
                        <p style="margin: 5px 0;">🕒 <strong>Data e Hora:</strong> %s</p>
                    </div>
                    
                    <p style="color: #555; font-size: 14px; line-height: 1.5;">
                        <strong>Foi você?</strong><br>
                        Se sim, pode ignorar este e-mail tranquilamente. Sua nova senha já está valendo.
                    </p>
                    <p style="color: #ef4444; font-size: 14px; line-height: 1.5;">
                        <strong>Não foi você?</strong><br>
                        Alguém pode ter acessado sua conta. Por favor, redefina sua senha imediatamente usando a opção "Esqueci minha senha" na tela de login.
                    </p>
                </body>
                </html>
                """, targetName, currentDateTime);

        sendEmailViaApi(targetEmail, targetName, subject, htmlContent);
        System.out.println("✅ E-mail de segurança (senha alterada) enviado para: " + targetEmail);
    }
}