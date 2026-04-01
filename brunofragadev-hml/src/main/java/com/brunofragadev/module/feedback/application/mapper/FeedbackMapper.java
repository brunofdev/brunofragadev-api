package com.brunofragadev.module.feedback.application.mapper;

import com.brunofragadev.module.feedback.domain.entity.Feedback;
import com.brunofragadev.module.feedback.api.dto.request.FeedbackCreateRequest;
import com.brunofragadev.module.feedback.api.dto.response.FeedbackDTO;
import com.brunofragadev.module.user.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeedbackMapper {

    public Feedback toNewFeedback(FeedbackCreateRequest request, User user) {
        return Feedback.criar(
                user,
                request.descricao(),
                request.avaliacao(),
                request.tipoFeedback(),
                request.referenciaId()
        );
    }

    // Utilizado ao criar ou atualizar um feedback individualmente.
    // (A listagem geral já é mapeada diretamente na Query do Repositório)
    public FeedbackDTO toDTO(Feedback feedback) {
        return new FeedbackDTO(
                feedback.getId(),
                feedback.getUser().getUsername(),
                feedback.getUser().getUsername(),
                feedback.getDescricao(),
                feedback.getAvaliacao(),
                feedback.getDataCriacao(),
                feedback.getUser().getFotoperfil(),
                feedback.getFeedbackType(),
                feedback.getReferenciaId(),
                feedback.getUser().getIsAnonimo()
        );
    }

    public List<FeedbackDTO> toDTOList(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(this::toDTO)
                .toList();
    }
}