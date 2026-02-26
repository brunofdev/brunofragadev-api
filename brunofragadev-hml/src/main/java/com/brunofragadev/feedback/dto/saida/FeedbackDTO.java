package com.brunofragadev.feedback.dto.saida;


import com.brunofragadev.feedback.entity.TipoFeedback;

import java.time.LocalDateTime;

public record FeedbackDTO(
        Long id,
        String criadoPor,
        String userName,
        String comentario,
        Integer notaAvaliacao,
        LocalDateTime dataDeCriacao,
        String fotoUsuario,
        TipoFeedback tipoFeedback,
        Long referenciaId,
        Boolean isAnonimo
) {
}
