package ru.extas.server.lead;

import ru.extas.model.lead.Lead;
import ru.extas.security.SecuredRepository;

import java.util.Set;

/**
 * <p>LeadService interface.</p>
 *
 * @author Valery Orlov
 *         Date: 19.12.13
 *         Time: 12:09
 * @version $Id: $Id
 * @since 0.3
 */
public interface LeadService extends SecuredRepository<Lead> {
    /**
     * Квалифицировать лид
     *
     * @param lead лид для квалификации
     */
    Lead qualify(Lead lead);

    /**
     * Завершает лид с заданным результатом
     *
     * @param lead лид
     * @param result результат завершения
     */
    void finishLead(Lead lead, Lead.Result result);

    /**
     * Возобновляет лид, перемещяя его в новые
     *
     * @param lead возобновляемый лид
     */
    void reopenLead(Lead lead);

    /**
     * Завершает набор лидов с указанным результатом
     *
     * @param leads набор лидов
     * @param result результат завершения
     */
    void finishLeads(Set<Lead> leads, Lead.Result result);
}
