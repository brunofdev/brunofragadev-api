package com.brunofragadev.module.user.domain.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public final class VerificationCode {

    @Column(name = "codigo_verificacao")
    private final String codigo;

    @Column(name = "expiracao_codigo")
    private final LocalDateTime expiracao ;

    private VerificationCode() {
        this.codigo = null;
        this.expiracao = null;
    }

    public VerificationCode(String codigo, LocalDateTime expiracao) {
        if (codigo == null || codigo.isBlank())
            throw new IllegalArgumentException("Codigo inválido");
        if (expiracao == null)
            throw new IllegalArgumentException("Expiração inválida");
        this.codigo = codigo;
        this.expiracao = expiracao;
    }

    public boolean estaExpirado() {
        return LocalDateTime.now().isAfter(this.expiracao);
    }

    public boolean corresponde(String tentativa) {
        return this.codigo.equals(tentativa);
    }

    public String getCodigo() {
        return codigo;
    }
    public LocalDateTime getExpiracao(){
        return expiracao;
    }
}
