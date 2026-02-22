package com.brunofragadev.feedback.dto;


import java.time.LocalDateTime;

public record FeedbackDTO(
        Long id,
        String criadoPor,
        String comentario,
        Integer notaAvaliacao,
        LocalDateTime dataDeCriacao,
        String fotoUsuario
) {
}
