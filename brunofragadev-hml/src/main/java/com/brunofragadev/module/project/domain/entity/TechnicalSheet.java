package com.brunofragadev.module.project.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class TechnicalSheet {
    private String linguagem;
    private String paradigma;
    private String framework;
    private String bibliotecas;
    private String infraestrutura;
}