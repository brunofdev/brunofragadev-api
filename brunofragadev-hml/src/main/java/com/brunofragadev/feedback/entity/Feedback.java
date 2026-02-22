package com.brunofragadev.feedback.entity;

import com.brunofragadev.security.auditoria.Auditable;
import com.brunofragadev.usuarios.entity.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "feedback")
public class Feedback extends Auditable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "avaliacao", nullable = false)
    private Integer avaliacao;

}
