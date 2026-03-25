package com.brunofragadev.module.project.domain.entity;

import com.brunofragadev.shared.domain.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projetos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    private String video;
    private String status;
    private String papel;
    private String dataProjeto;
    private String repositorioUrl;
    private String liveUrl;


    @Embedded
    private TechnicalSheet techs;


    @Embedded
    private ProjectSetup setup;


    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordemExibicao ASC")
    private List<ProjectImage> galeria = new ArrayList<>();


    public void setGaleria(List<ProjectImage> imagens) {
        this.galeria.clear();
        if (imagens != null) {
            imagens.forEach(img -> img.setProject(this));
            this.galeria.addAll(imagens);
        }
    }
}