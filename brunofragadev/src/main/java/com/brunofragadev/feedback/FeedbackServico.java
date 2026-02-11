package com.brunofragadev.feedback;

import com.brunofragadev.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServico {

    @Autowired
    private FeedbackRepositorio feedbackRepositorio;
    @Autowired
    private FeedbackMapeador feedbackMapeador;

    public FeedbackDTO criarFeedback(CriarFeedbackDTO dto, Usuario usuario){
        Feedback feedback = feedbackMapeador.mapearFeedbackCriacao(dto, usuario);
        return feedbackMapeador.mapearFeedbackDTO(feedback);
    }
    public List<FeedbackDTO> listarFeedbacks (){
        return feedbackMapeador.mapearListaDeFeedbacks(feedbackRepositorio.findAll());
    }

}
