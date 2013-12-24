package ru.extas.server;

import ru.extas.model.Insurance;

/**
 * @author Valery Orlov
 *         Date: 19.12.13
 *         Time: 11:55
 */
public interface InsuranceService {
/**
 * Возвращает список всех зарегестрированных страховок
 *
 * @return список страховок
 */
Iterable<Insurance> loadAll();

/**
 * Сохраняет и выпускает полис страхования
 *
 * @param insurance что сохраняем
 */
void saveAndIssue(Insurance insurance);

}
