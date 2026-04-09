package com.brunofragadev.infrastructure.email;

import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.user.api.dto.response.UserDTO;
import com.brunofragadev.module.user.domain.entity.VerificationCode;
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
        String subject = "Código de Verificação - Bruno Fraga Dev";
        String html = """
            <html>
            <body style="font-family: Arial, sans-serif; color: #333; margin: 0; padding: 20px; background-color: #f9f9f9;">
                <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; border: 1px solid #e0e0e0; border-radius: 8px; padding: 30px;">
                    <h2 style="color: #181818;">Olá, %s!</h2>
                    <p>Obrigado por se cadastrar na plataforma Bruno Fraga Dev. Para continuar, digite o código de verificação abaixo:</p>
                    
                    <div style="background-color: #181818; padding: 20px; border-radius: 8px; text-align: center; margin: 30px 0;">
                        <h1 style="color: #4caf50; letter-spacing: 10px; margin: 0; font-size: 36px;">%s</h1>
                    </div>
                    
                    <p style="color: #f50505; font-size: 14px; font-weight: bold;">Este código expira em 15 minutos.</p>
                    
                    <hr style="border: none; border-top: 1px solid #eee; margin-top: 30px;">
                    <p style="font-size: 12px; color: #777; text-align: center;">Acesse <a href="https://www.brunofragadev.com" style="color: #4caf50; text-decoration: none; font-weight: bold;">brunofragadev.com</a></p>
                </div>
            </body>
            </html>
            """.formatted(targetName, verificationCode);

        emailProvider.send(targetEmail, targetName, subject, html);
    }

    // 2. BOAS-VINDAS
    public void sendWelcomeEmail(String targetEmail, String targetName) {
        String subject = "Seja bem-vindo(a)! - Bruno Fraga Dev";
        String html = """
            <html>
            <body style="font-family: Arial, sans-serif; color: #333; margin: 0; padding: 20px; background-color: #f9f9f9;">
                <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; border: 1px solid #e0e0e0; border-radius: 8px; padding: 30px;">
                    <h2 style="color: #181818;">Cadastro Concluído!</h2>
                    <p>Olá, <strong>%s</strong>!</p>
                    <p>Sua conta foi verificada com sucesso. Ficamos muito felizes em ter você conosco.</p>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="https://www.brunofragadev.com/login" style="background-color: #4caf50; color: #ffffff; padding: 15px 30px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;">Acessar Plataforma</a>
                    </div>
                    
                    <p>Prepare-se para explorar nossos projetos e artigos.</p>
                    
                    <hr style="border: none; border-top: 1px solid #eee; margin-top: 30px;">
                    <p style="font-size: 12px; color: #777; text-align: center;">Acesse <a href="https://www.brunofragadev.com" style="color: #4caf50; text-decoration: none; font-weight: bold;">brunofragadev.com</a></p>
                </div>
            </body>
            </html>
            """.formatted(targetName);

        emailProvider.send(targetEmail, targetName, subject, html);
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
    public void sendPasswordRecoveryEmail(String targetEmail, String targetName, String recoveryCode) {
        String subject = "Recuperação de Senha - Bruno Fraga Dev";
        String html = """
            <html>
            <body style="font-family: Arial, sans-serif; color: #333; margin: 0; padding: 20px; background-color: #f9f9f9;">
                <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; border: 1px solid #e0e0e0; border-radius: 8px; padding: 30px;">
                    <h2 style="color: #181818;">Recuperação de Senha</h2>
                    <p>Olá, %s. Recebemos um pedido para redefinir a sua senha.</p>
                    <p>Utilize o código abaixo para criar uma nova senha:</p>
                    
                    <div style="background-color: #181818; padding: 20px; border-radius: 8px; text-align: center; margin: 30px 0;">
                        <h1 style="color: #4caf50; letter-spacing: 10px; margin: 0; font-size: 36px;">%s</h1>
                    </div>
                    
                    <p style="color: #f50505; font-size: 14px; font-weight: bold;">Este código expira em 15 minutos.</p>
                    <p style="font-size: 14px; color: #666;">Se você não solicitou essa alteração, apenas ignore este e-mail.</p>
                    
                    <hr style="border: none; border-top: 1px solid #eee; margin-top: 30px;">
                    <p style="font-size: 12px; color: #777; text-align: center;">Acesse <a href="https://www.brunofragadev.com" style="color: #4caf50; text-decoration: none; font-weight: bold;">brunofragadev.com</a></p>
                </div>
            </body>
            </html>
            """.formatted(targetName, recoveryCode);

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