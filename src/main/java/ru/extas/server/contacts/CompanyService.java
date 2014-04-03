package ru.extas.server.contacts;

import ru.extas.model.contacts.Company;
import ru.extas.security.SecuredRepository;

/**
 * Сервис обработки данных компании
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 13:53
 */
public interface CompanyService extends SecuredRepository<Company> {
}
