package com.brunofragadev.module.feedback.domain.entity;

import com.brunofragadev.module.user.domain.entity.User;
import com.brunofragadev.shared.domain.entity.Auditable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "feedback")
@EqualsAndHashCode(of = "id", callSuper = false)
public class Feedback extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "avaliacao", nullable = false)
    private Integer avaliacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_feedback", nullable = false)
    private FeedbackType feedbackType;

    // Se for GERAL (Home), fica zero. Se for Projeto ou artigo , guarda o ID de referencia.
    @Column(name = "referencia_id", nullable = true)
    private Long referenciaId;

    public static Feedback criar(User user, String descricao, Integer avaliacao, FeedbackType feedbackType, Long referenciaId){
        Feedback f = new Feedback();
        validarDadosCriacao(user, descricao, avaliacao);
        f.user = user;
        f.descricao = descricao;
        f.avaliacao = avaliacao;
        f.feedbackType = (feedbackType == null) ? FeedbackType.GERAL : feedbackType;
        f.referenciaId = (referenciaId == null) ? 0 : referenciaId;
        return f;
    }

    private static void validarUsuario(User user) {
        if (user == null)
            throw new IllegalArgumentException("Usuário não pode ser nulo");
    }
    private static void validarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank())
            throw new IllegalArgumentException("Descrição não pode ser vazia");
    }
    private static void validarAvaliacao(Integer avaliacao) {
        if (avaliacao == null || avaliacao < 1 || avaliacao > 5)
            throw new IllegalArgumentException("Avaliação deve ser entre 1 e 5");
    }
    private static void validarDadosCriacao(User user, String descricao, Integer avaliacao ){
        validarUsuario(user);
        validarDescricao(descricao);
        validarAvaliacao(avaliacao);
    }

    public void setDescricao(String descricao) {
        validarDescricao(descricao);
        this.descricao = descricao;
    }

    public void setAvaliacao(Integer avaliacao) {
        validarAvaliacao(avaliacao);
        this.avaliacao = avaliacao;
    }
}