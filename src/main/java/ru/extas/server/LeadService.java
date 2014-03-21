package ru.extas.server;

import ru.extas.model.Lead;

/**
 * <p>LeadService interface.</p>
 *
 * @author Valery Orlov
 *         Date: 19.12.13
 *         Time: 12:09
 * @version $Id: $Id
 * @since 0.3
 */
public interface LeadService {
/**
 * Квалифицировать лид
 *
 * @param obj лид для квалификации
 */
void qualify(Lead obj);
}
