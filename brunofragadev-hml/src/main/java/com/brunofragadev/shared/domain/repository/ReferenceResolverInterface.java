package com.brunofragadev.shared.domain.repository;

import com.brunofragadev.module.feedback.domain.entity.FeedbackType;
import com.brunofragadev.shared.application.service.ArticleReferenceResolver;
import com.brunofragadev.shared.application.service.GeneralPageReferenceResolver;
import com.brunofragadev.shared.application.service.ProjectReferenceResolver;
import com.brunofragadev.shared.application.service.ReferenceResolverFactory;

/**
 * Contrato para resolução de nomes de referência associados a feedbacks.
 *
 * <p>Cada implementação é responsável por resolver o nome de exibição
 * de um tipo específico de {@link FeedbackType}. Novas implementações
 * devem ser anotadas com {@code @Component} para que o Spring as registre
 * automaticamente na {@code ReferenceResolverFactory}.</p>
 *
 * <h3>Implementações disponíveis:</h3>
 * <ul>
 *   <li>{@link ArticleReferenceResolver} — resolve referências do tipo {@code ARTIGO}</li>
 *   <li>{@link ProjectReferenceResolver} — resolve referências do tipo {@code PROJETO}</li>
 *   <li>{@link GeneralPageReferenceResolver} — resolve referências do tipo {@code GERAL}</li>
 * </ul>
 *
 * <h3>Como adicionar um novo tipo:</h3>
 * <ol>
 *   <li>Crie uma classe em {@code shared/application/service}</li>
 *   <li>Implemente esta interface</li>
 *   <li>Anote com {@code @Component}</li>
 *   <li>Implemente {@code getType()} retornando o {@link FeedbackType} correspondente</li>
 * </ol>
 *
 * @see ReferenceResolverFactory
 * @see FeedbackType
 */
public interface ReferenceResolverInterface {

    /**
     * Retorna o tipo de feedback que esta implementação é capaz de resolver.
     * Usado pela {@link ReferenceResolverFactory} para montar o mapa de resolvers.
     *
     * @return o {@link FeedbackType} correspondente a esta implementação
     */
    FeedbackType getType();

    /**
     * Resolve o nome de exibição da referência associada ao feedback.
     *
     * @param referenceId o identificador da entidade referenciada — pode ser nulo para {@code GERAL}
     * @return string formatada para exibição, nunca {@code null}
     */
    String resolveName(Long referenceId);
}
