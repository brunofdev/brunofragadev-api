package com.brunofragadev.module.project.domain.entity;

import com.brunofragadev.shared.domain.entity.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_imagens_projetos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectImage extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String urlImagem;

    @Column(nullable = false)
    private Integer ordemExibicao;

    @Column(length = 255)
    private String legenda;

    private Boolean isCapa = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id", nullable = false)
    @JsonIgnore
    private Project project;
}