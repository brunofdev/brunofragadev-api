package com.brunofragadev.module.project.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
public class ProjectSetup {
    private String obs;


    @ElementCollection
    @CollectionTable(name = "tb_passos_setup", joinColumns = @JoinColumn(name = "projeto_id"))
    @OrderBy("num ASC")
    private List<SetupStep> steps = new ArrayList<>();
}