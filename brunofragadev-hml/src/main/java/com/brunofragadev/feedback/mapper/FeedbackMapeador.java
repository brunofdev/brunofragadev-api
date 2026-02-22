package com.brunofragadev.feedback.mapper;

import com.brunofragadev.feedback.entity.Feedback;
import com.brunofragadev.feedback.dto.CriarFeedbackDTO;
import com.brunofragadev.feedback.dto.FeedbackDTO;
import com.brunofragadev.usuarios.entity.Usuario;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class FeedbackMapeador {

    public Feedback mapearFeedbackCriacao(CriarFeedbackDTO dto, Usuario usuario){
        Feedback feedback = new Feedback();
        feedback.setUsuario(usuario);
        feedback.setDescricao(dto.descricao());
        feedback.setAvaliacao(dto.avaliacao());
        return feedback;
    }
    //VERIFICAR, POIS EXISTE UM CONSULTA NO REPOSITORIO QUE RETORNA UM DTO DIRETAMENTE, NO UNICO CASO DE USO ATE O MOMENTO
    public FeedbackDTO mapearFeedbackDTO(Feedback feedback){
        return new FeedbackDTO(
                feedback.getId(),
                feedback.getUsuario().getUsername(),
                feedback.getDescricao(),
                feedback.getAvaliacao(),
                feedback.getDataCriacao(),
                "" // POR ISSO A STRING VAZIA AQUI, ESTE METODO NAO ESTA SENDO UTILIZADO
        );
    }
    public List<FeedbackDTO> mapearListaDeFeedbacks(List<Feedback> feedbacks){
        return feedbacks.stream()
                .map(this::mapearFeedbackDTO).toList();
    }
}
