package com.brunofragadev.shared.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
    @CreatedDate
    @Column(name = "data_criacao", nullable = false, updatable = false)
    protected LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    protected LocalDateTime dataAtualizacao;

    @CreatedBy
    @Column(name = "criado_por", updatable = false)
    protected String criadoPor;

    @LastModifiedBy
    @Column(name = "atualizado_por")
    protected String atualizadoPor;
}


