package com.brunofragadev.infrastructure.log.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs_erro")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private String endpoint;

    @Column(nullable = false, length = 10)
    private String metodoHttp;

    @Column(nullable = false, length = 500)
    private String mensagemResumo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String stackTraceCompleta;

    @Column(length = 100)
    private String usuarioLogado;

    public static ErrorLog register(Exception ex, String endpoint, String httpMethod, String loggedUser) {
        ErrorLog log = new ErrorLog();
        log.dataHora = LocalDateTime.now();
        log.endpoint = endpoint;
        log.metodoHttp = httpMethod;
        log.usuarioLogado = loggedUser;

        log.mensagemResumo = extractSafeMessage(ex);
        log.stackTraceCompleta = extractStackTrace(ex);

        return log;
    }

    private static String extractSafeMessage(Exception ex) {
        String msg = ex.getMessage();
        if (msg == null) return ex.getClass().getSimpleName();
        return msg.length() > 500 ? msg.substring(0, 497) + "..." : msg;
    }

    private static String extractStackTrace(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}