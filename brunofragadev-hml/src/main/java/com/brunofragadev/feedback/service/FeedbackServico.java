package com.brunofragadev.feedback.service;

import com.brunofragadev.feedback.repository.FeedbackRepositorio;
import com.brunofragadev.feedback.dto.entrada.CriarFeedbackDTO;
import com.brunofragadev.feedback.dto.saida.FeedbackDTO;
import com.brunofragadev.feedback.entity.Feedback;
import com.brunofragadev.feedback.mapper.FeedbackMapeador;
import com.brunofragadev.usuarios.entity.Role;
import com.brunofragadev.usuarios.entity.Usuario;
import com.brunofragadev.usuarios.exceptions.InvalidCredentialsException;
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

    public List<FeedbackDTO> listarFeedbacksTipoGeral (){
        return feedbackRepositorio.buscarTodosTipoGeralComFotos();
    }

    public List<FeedbackDTO> listarFeedbacksTipoProjeto (Long idProjeto) {
        return feedbackRepositorio.buscarTodosTipoProjetoComFotos(idProjeto);
    }

    public FeedbackDTO atualizarFeedback(Long idFeedback, CriarFeedbackDTO dto, Usuario usuario) {
        Feedback feedback = feedbackRepositorio.findById(idFeedback)
                .orElseThrow(() -> new RuntimeException("Feedback não encontrado!"));
        if (!feedback.getUsuario().getId().equals(usuario.getId())) {
            throw new InvalidCredentialsException("Acesso negado: Você não tem permissão para editar este feedback.");
        }
        feedback.setDescricao(dto.descricao());
        feedback.setAvaliacao(dto.avaliacao());
        feedbackRepositorio.save(feedback);
        return feedbackMapeador.mapearFeedbackDTO(feedback);
    }
    public void excluirFeedback(Long idFeedback, Usuario usuario) {
        Feedback feedback = feedbackRepositorio.findById(idFeedback)
                .orElseThrow(() -> new RuntimeException("Feedback não encontrado!"));

        boolean isDonoDoComentario = feedback.getUsuario().getId().equals(usuario.getId());
        boolean isAdmin = usuario.getRole() == Role.ADMIN3; // Ajuste o getRole() se o seu método chamar de forma diferente

        if (!isDonoDoComentario && !isAdmin) {
            throw new RuntimeException("Acesso negado: Você não tem permissão para excluir este feedback.");
        }

        feedbackRepositorio.delete(feedback);
    }
}