package com.brunofragadev.infrastructure.email;

import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private final EmailProvider emailProvider;

    public EmailService(EmailProvider emailProvider) {
        this.emailProvider = emailProvider;
    }

    // 1. CÓDIGO DE VERIFICAÇÃO
    public void sendVerificationEmail(String targetEmail, String targetName, String verificationCode) {
        String subject = "Código de Verificação - Bruno Dev";
        String html = String.format("""
                <html>
                <body style="font-family: Arial, sans-serif; color: #333;">
                    <h2>Olá %s,</h2>
                    <p>Obrigado por se cadastrar! Digite o código abaixo:</p>
                    <div style="background-color: #181818; padding: 20px; border-radius: 8px; text-align: center; margin: 20px 0;">
                        <h1 style="color: #4caf50; letter-spacing: 10px; margin: 0; font-size: 36px;">%s</h1>
                    </div>
                    <p style="color: #f50505; font-size: 12px;">Expira em 15 minutos.</p>
                </body>
                </html>
                """, targetName, verificationCode);

        emailProvider.send(targetEmail, targetName, subject, html);
    }

    // 2. BOAS-VINDAS
    public void sendWelcomeEmail(UserDTO user) {
        String subject = "Seja bem-vindo(a), " + user.nome() + "!";
        String html = String.format("<html><body><h1>Olá %s!</h1><p>Sua conta foi verificada com sucesso!</p></body></html>", user.nome());

        emailProvider.send(user.email(), user.nome(), subject, html);
    }

    // 3. ALERTA PARA O ADMIN
    public void sendNewFeedbackAlertToAdmin(FeedbackDTO feedback, String feedbackLocation) {
        String subject = "Novo Feedback Recebido!";
        String html = String.format("""
                <html><body>
                    <h2>Olá Bruno, novo feedback de: %s</h2>
                    <p><strong>Nota:</strong> %d</p>
                    <p><strong>Comentário:</strong> %s</p>
                    <p><strong>Local:</strong> %s</p>
                </body></html>
                """, feedback.criadoPor(), feedback.notaAvaliacao(), feedback.comentario(), feedbackLocation);

        emailProvider.send("brunofragaa97@gmail.com", "Bruno Fraga (Admin)", subject, html);
    }

    // 4. AGRADECIMENTO AO USUÁRIO
    public void sendFeedbackThankYouEmail(UserDTO user) {
        String subject = "Feedback postado — obrigado!";
        String html = String.format("<html><body><h2>Olá %s,</h2><p>Obrigado por ajudar a evoluir o projeto!</p></body></html>", user.nome());

        emailProvider.send(user.email(), user.nome(), subject, html);
    }

    // 5. RECUPERAÇÃO DE SENHA
    public void sendPasswordRecoveryEmail(String targetEmail, String targetName, String verificationCode) {
        String subject = "Recuperação de Senha - Bruno Dev";
        String html = String.format("""
                <html><body>
                    <h2>Olá %s,</h2>
                    <p>Use o código abaixo para redefinir sua senha:</p>
                    <div style="background-color: #181818; padding: 20px; text-align: center;">
                        <h1 style="color: #4caf50;">%s</h1>
                    </div>
                </body></html>
                """, targetName, verificationCode);

        emailProvider.send(targetEmail, targetName, subject, html);
    }

    // 6. LEMBRETE DE USERNAME
    public void sendUsernameReminderEmail(String targetEmail, String targetName, String userName) {
        String subject = "Seu Nome de Usuário - Bruno Dev";
        String html = String.format("<html><body><h2>Olá %s,</h2><p>Seu login é: <strong>%s</strong></p></body></html>", targetName, userName);

        emailProvider.send(targetEmail, targetName, subject, html);
    }

    // 7. CONFIRMAÇÃO DE ALTERAÇÃO DE SENHA
    public void sendPasswordChangedSuccessfullyEmail(String targetEmail, String targetName) {
        String subject = "Alerta de Segurança: Sua senha foi alterada";
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String html = String.format("<html><body><h2>Olá %s,</h2><p>Sua senha foi alterada em %s.</p></body></html>", targetName, now);

        emailProvider.send(targetEmail, targetName, subject, html);
    }
}