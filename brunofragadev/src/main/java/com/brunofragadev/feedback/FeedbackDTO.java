package com.brunofragadev.feedback;


import java.time.LocalDateTime;

public record FeedbackDTO(
        String criadoPor,
        String comentario,
        Integer notaAvaliacao,
        LocalDateTime dataDeCriacao
) {
}
