package ru.extas.server.lead;

import ru.extas.model.lead.Lead;
import ru.extas.security.SecuredRepository;

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
     * @param obj лид для квалификации
     */
    void qualify(Lead obj);
}
