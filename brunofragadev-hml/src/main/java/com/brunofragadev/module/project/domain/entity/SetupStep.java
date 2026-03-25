package com.brunofragadev.module.project.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class SetupStep {
    private Integer num;
    private String text;
    private String cmd;
}