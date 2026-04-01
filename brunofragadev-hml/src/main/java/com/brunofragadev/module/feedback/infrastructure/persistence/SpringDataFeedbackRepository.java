package com.brunofragadev.module.feedback.infrastructure.persistence;

import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.feedback.domain.entity.Feedback;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataFeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("""
        SELECT new com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO(
            f.id,
            u.nomePublico,
            u.userName,
            f.descricao,
            f.avaliacao,
            f.dataCriacao,
            u.fotoperfil,
            f.feedbackType,
            f.referenciaId,
            u.isAnonimo
        )
        FROM Feedback f
        JOIN f.user u
        WHERE f.feedbackType = com.brunofragadev.module.feedback.domain.entity.FeedbackType.GERAL
        ORDER BY f.dataCriacao DESC
    """)
    List<FeedbackDTO> findGeneralFeedbacksWithPhotos();

    @Query("""
        SELECT new com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO(
            f.id,
            u.nomePublico,
            u.userName,
            f.descricao,
            f.avaliacao,
            f.dataCriacao,
            u.fotoperfil,
            f.feedbackType,
            f.referenciaId,
            u.isAnonimo
        )
        FROM Feedback f
        JOIN f.user u
        WHERE f.feedbackType = com.brunofragadev.module.feedback.domain.entity.FeedbackType.PROJETO
        AND f.referenciaId = :projetoId
        ORDER BY f.dataCriacao DESC
    """)
    List<FeedbackDTO> findProjectFeedbacksWithPhotos(@Param("projetoId") Long projetoId);
    @Query("""
        SELECT new com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO(
            f.id,
            u.nomePublico,
            u.userName,
            f.descricao,
            f.avaliacao,
            f.dataCriacao,
            u.fotoperfil,
            f.feedbackType,
            f.referenciaId,
            u.isAnonimo
        )
        FROM Feedback f
        JOIN f.user u
        WHERE f.feedbackType = com.brunofragadev.module.feedback.domain.entity.FeedbackType.ARTIGO
        AND f.referenciaId = :artigoId
        ORDER BY f.dataCriacao DESC
    """)
    List<FeedbackDTO> findArticleFeedbacksWithPhotos(@Param("artigoId") Long artigoId);
    List<Feedback> findAllByReferenciaId(Long referenciaId);
    @Modifying
    @Query("DELETE FROM Feedback f WHERE f.referenciaId = :referenciaId")
    void deleteAllByReferenciaId(@Param("referenciaId") Long referenciaId);
}