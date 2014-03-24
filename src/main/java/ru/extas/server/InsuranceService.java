package ru.extas.server;

import ru.extas.model.insurance.Insurance;

/**
 * <p>InsuranceService interface.</p>
 *
 * @author Valery Orlov
 *         Date: 19.12.13
 *         Time: 11:55
 * @version $Id: $Id
 * @since 0.3
 */
public interface InsuranceService {

/**
 * Сохраняет и выпускает полис страхования
 *
 * @param insurance что сохраняем
 */
void saveAndIssue(Insurance insurance);

}
