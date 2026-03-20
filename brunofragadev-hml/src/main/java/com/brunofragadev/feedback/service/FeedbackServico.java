package com.brunofragadev.feedback.service;

import com.brunofragadev.email.ServicoDeEmail;
import com.brunofragadev.feedback.repository.FeedbackRepositorio;
import com.brunofragadev.feedback.dto.entrada.CriarFeedbackDTO;
import com.brunofragadev.feedback.dto.saida.FeedbackDTO;
import com.brunofragadev.feedback.entity.Feedback;
import com.brunofragadev.feedback.mapper.FeedbackMapeador;
import com.brunofragadev.projetos.entitys.Projeto;
import com.brunofragadev.shared.service.ResolvedorDeReferencia;
import com.brunofragadev.usuarios.entity.Role;
import com.brunofragadev.usuarios.entity.Usuario;
import com.brunofragadev.usuarios.exceptions.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServico {


    private final FeedbackRepositorio feedbackRepositorio;
    private final FeedbackMapeador feedbackMapeador;
    private final ServicoDeEmail servicoDeEmail;
    private final ResolvedorDeReferencia resolvedorDeReferencia;

    public FeedbackServico (FeedbackRepositorio feedbackRepositorio, FeedbackMapeador feedbackMapeador, ServicoDeEmail servicoDeEmail, ResolvedorDeReferencia resolvedorDeReferencia){
        this.feedbackRepositorio = feedbackRepositorio;
        this.feedbackMapeador = feedbackMapeador;
        this.servicoDeEmail = servicoDeEmail;
        this.resolvedorDeReferencia = resolvedorDeReferencia;
    }

    public FeedbackDTO criarFeedback(CriarFeedbackDTO dto, Usuario usuario){
        Feedback feedback = feedbackMapeador.mapearFeedbackCriacao(dto, usuario);
        feedbackRepositorio.save(feedback);
        FeedbackDTO feedbackDTO = feedbackMapeador.mapearFeedbackDTO(feedback);
        String localFeedback = resolvedorDeReferencia.resolverNome(feedbackDTO.tipoFeedback(), feedbackDTO.referenciaId());
        servicoDeEmail.enviarAlertaDeNovoFeedbackParaAdmin(feedbackDTO, localFeedback);
        return feedbackDTO;
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