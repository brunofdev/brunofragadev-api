package com.brunofragadev.module.feedback.api.dto.request;

import com.brunofragadev.module.feedback.domain.entity.FeedbackType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "DTO para criação de um feedback")
public record FeedbackCreateRequest(
        @Schema(description = "Descrição do feedback", example = "O projeto ficou sensacional e muito bem estruturado!")
        @NotBlank(message = "A descrição não pode estar em branco")
        @Size(min = 10, max = 500, message = "A descrição deve ter entre 10 e 500 caracteres.") // 👈 A regra adicionada
        @Pattern(regexp = "^[a-zA-Z0-9\\s\\p{P}áàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ]+$", message = "A descrição contém caracteres inválidos")
        String descricao,

        @Schema(description = "Avaliação do feedback (de 1 a 5)", example = "5", minimum = "1", maximum = "5")
        @NotNull(message = "A avaliação é obrigatória")
        @Min(value = 1, message = "A avaliação deve ser no mínimo 1")
        @Max(value = 5, message = "A avaliação deve ser no máximo 5")
        Integer avaliacao,

        @Schema(description = "Tipo de feedback (pode ser null, será salvo na página principal)", nullable = false)
        FeedbackType tipoFeedback,

        @Schema(description = "ID de referência", example = "123")
        @PositiveOrZero(message = "O ID de referência deve ser 0(zero para pagina geral) ou positivo para feedbacks em projetos ou artigos")
        Long referenciaId
) {
}