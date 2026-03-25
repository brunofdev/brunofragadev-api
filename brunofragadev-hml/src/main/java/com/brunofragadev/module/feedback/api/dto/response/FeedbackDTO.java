package com.brunofragadev.module.feedback.api.dto.response;


import com.brunofragadev.module.feedback.domain.entity.FeedbackType;

import java.time.LocalDateTime;

public record FeedbackDTO(
        Long id,
        String criadoPor,
        String userName,
        String comentario,
        Integer notaAvaliacao,
        LocalDateTime dataDeCriacao,
        String fotoUsuario,
        FeedbackType feedbackType,
        Long referenciaId,
        Boolean isAnonimo
) {
}
