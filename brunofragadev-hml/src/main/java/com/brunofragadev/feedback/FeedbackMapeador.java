package com.brunofragadev.feedback;

import com.brunofragadev.usuarios.Usuario;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class FeedbackMapeador {

    public  Feedback mapearFeedbackCriacao(CriarFeedbackDTO dto, Usuario usuario){
        Feedback feedback = new Feedback();
        feedback.setUsuario(usuario);
        feedback.setDescricao(dto.descricao());
        feedback.setAvaliacao(dto.avaliacao());
        return feedback;
    }
    public FeedbackDTO mapearFeedbackDTO(Feedback feedback){
        return new FeedbackDTO(
                feedback.getId(),
                feedback.getUsuario().getUsername(),
                feedback.getDescricao(),
                feedback.getAvaliacao(),
                feedback.getDataCriacao()
        );
    }
    public List<FeedbackDTO> mapearListaDeFeedbacks(List<Feedback> feedbacks){
        return feedbacks.stream()
                .map(this::mapearFeedbackDTO).toList();
    }
}
