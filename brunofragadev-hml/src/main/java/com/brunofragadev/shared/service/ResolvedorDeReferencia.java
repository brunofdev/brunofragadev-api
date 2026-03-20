package com.brunofragadev.shared.service;

import com.brunofragadev.feedback.entity.TipoFeedback;
import com.brunofragadev.projetos.entitys.Projeto;
import com.brunofragadev.projetos.repository.ProjetoRepository;
import org.springframework.stereotype.Service;

@Service
public class ResolvedorDeReferencia {

    private final ProjetoRepository projetoRepository;

    public ResolvedorDeReferencia (ProjetoRepository projetoRepository){
        this.projetoRepository = projetoRepository;
    }

    public String resolverNome(TipoFeedback tipo, Long referenciaId) {
        if (tipo == TipoFeedback.GERAL || referenciaId == null) {
            return "Página geral";
        }
        if (tipo == TipoFeedback.PROJETO) {
            return "Postado no projeto: " + projetoRepository.findById(referenciaId)
                    .map(Projeto::getTitle)
                    .orElse("Projeto não encontrado");
        }
        return "Referência desconhecida";
    }

}
