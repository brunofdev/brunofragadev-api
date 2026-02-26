package com.brunofragadev.feedback.dto.entrada;

import com.brunofragadev.feedback.entity.TipoFeedback;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size; // 👈 Importação necessária

@Schema(description = "DTO para criação de um feedback")
public record CriarFeedbackDTO(
        @Schema(description = "Descrição do feedback", example = "O projeto ficou sensacional e muito bem estruturado!")
        @NotBlank(message = "A descrição não pode estar em branco")
        @Size(min = 15, max = 1000, message = "A descrição deve ter entre 15 e 1000 caracteres.") // 👈 A regra adicionada
        @Pattern(regexp = "^[a-zA-Z0-9\\s\\p{P}áàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ]+$", message = "A descrição contém caracteres inválidos")
        String descricao,

        @Schema(description = "Avaliação do feedback (de 1 a 5)", example = "5", minimum = "1", maximum = "5")
        @NotNull(message = "A avaliação é obrigatória")
        @Min(value = 1, message = "A avaliação deve ser no mínimo 1")
        @Max(value = 5, message = "A avaliação deve ser no máximo 5")
        Integer avaliacao,

        @Schema(description = "Tipo de feedback (pode ser null, será salvo na página principal)", nullable = true)
        // pode receber null, sera salvo na pagina principal
        TipoFeedback tipoFeedback,

        @Schema(description = "ID de referência", example = "123")
        @Positive(message = "O ID de referência deve ser positivo")
        Long referenciaId
) {
}