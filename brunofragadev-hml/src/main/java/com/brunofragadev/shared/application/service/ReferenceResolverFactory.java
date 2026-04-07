package com.brunofragadev.shared.application.service;

import com.brunofragadev.module.feedback.domain.entity.FeedbackType;
import com.brunofragadev.shared.domain.repository.ReferenceResolverInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Fábrica dinâmica que gerencia as estratégias de resolução de nomes para referências de feedback
 * por tipo de feedback.
 *
 * Esta classe utiliza o padrão Strategy para desacoplar a lógica de busca de nomes
 * (Artigos, Projetos, etc.) do fluxo principal de feedbacks.
 */
@Service
public class ReferenceResolverFactory {

    // Mapa que vincula cada tipo de feedback à sua implementação específica de resolução
    private final Map<FeedbackType, ReferenceResolverInterface> resolvers;

    /**
     * O Spring injeta automaticamente todas as implementações de ReferenceResolverInterface
     * encontradas no contexto da aplicação.
     */
    public ReferenceResolverFactory(List<ReferenceResolverInterface> resolvers) {
        this.resolvers = resolvers.stream()
                .collect(Collectors.toMap(
                        ReferenceResolverInterface::getType,
                        r -> r
                ));
    }

    /**
     * Resolve o nome amigável de uma referência (ex: Título de um Artigo) com base no seu ID.
     * @param type O tipo do feedback que define qual estratégia de busca usar.
     * @param referenceId O ID do objeto referenciado no banco de dados.
     * @return O nome resolvido ou um valor padrão caso a estratégia específica não exista.
     */
    public String resolveName(FeedbackType type, Long referenceId) {
        return resolvers
                .getOrDefault(type, new GeneralPageReferenceResolver())
                .resolveName(referenceId);
    }
}