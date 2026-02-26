package com.brunofragadev.feedback.repository;

import com.brunofragadev.feedback.dto.saida.FeedbackDTO;
import com.brunofragadev.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepositorio extends JpaRepository<Feedback, Long> {
    @Query("""
        SELECT new com.brunofragadev.feedback.dto.saida.FeedbackDTO(
            f.id,
            u.nomePublico,
            u.userName,
            f.descricao,
            f.avaliacao,
            f.dataCriacao,
            u.fotoperfil,
            f.tipoFeedback,
            f.referenciaId,
            u.isAnonimo
        )
        FROM Feedback f
        JOIN f.usuario u
        WHERE f.tipoFeedback = com.brunofragadev.feedback.entity.TipoFeedback.GERAL
        ORDER BY f.dataCriacao DESC
    """)
    List<FeedbackDTO> buscarTodosTipoGeralComFotos();

    @Query("""
        SELECT new com.brunofragadev.feedback.dto.saida.FeedbackDTO(
            f.id,
            u.nomePublico,
            u.userName,
            f.descricao,
            f.avaliacao,
            f.dataCriacao,
            u.fotoperfil,
            f.tipoFeedback,
            f.referenciaId,
            u.isAnonimo
        )
        FROM Feedback f
        JOIN f.usuario u
        WHERE f.tipoFeedback = com.brunofragadev.feedback.entity.TipoFeedback.PROJETO
        AND f.referenciaId = :projetoId
        ORDER BY f.dataCriacao DESC
    """)
    List<FeedbackDTO> buscarTodosTipoProjetoComFotos(@org.springframework.data.repository.query.Param("projetoId") Long projetoId);

}
