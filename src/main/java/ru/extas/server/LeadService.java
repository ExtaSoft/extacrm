package ru.extas.server;

import ru.extas.model.Lead;

/**
 * Служба управления лидами
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 22:52
 */
public interface LeadService {

    /**
     * Сохранить лид
     *
     * @param obj лид для сохранения
     */
    public void persist(Lead obj);

    /**
     * Квалифицировать лид
     *
     * @param obj лид для квалификации
     */
    void qualify(Lead obj);
}
