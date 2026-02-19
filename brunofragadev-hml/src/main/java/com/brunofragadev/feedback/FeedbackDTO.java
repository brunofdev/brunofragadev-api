package com.brunofragadev.feedback;


import java.time.LocalDateTime;

public record FeedbackDTO(
        Long id,
        String criadoPor,
        String comentario,
        Integer notaAvaliacao,
        LocalDateTime dataDeCriacao
) {
}
