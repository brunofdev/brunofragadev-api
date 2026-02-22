package com.brunofragadev.feedback.service;

import com.brunofragadev.feedback.repository.FeedbackRepositorio;
import com.brunofragadev.feedback.dto.CriarFeedbackDTO;
import com.brunofragadev.feedback.dto.FeedbackDTO;
import com.brunofragadev.feedback.entity.Feedback;
import com.brunofragadev.feedback.mapper.FeedbackMapeador;
import com.brunofragadev.usuarios.entity.Usuario;
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
        feedbackRepositorio.save(feedback);
        return feedbackMapeador.mapearFeedbackDTO(feedback);
    }
    public List<FeedbackDTO> listarFeedbacks (){
        return feedbackRepositorio.buscarTodosComFotos();
    }

}
