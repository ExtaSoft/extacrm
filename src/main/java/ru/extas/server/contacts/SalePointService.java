package ru.extas.server.contacts;

import ru.extas.model.contacts.SalePoint;
import ru.extas.security.SecuredRepository;

/**
 * Интерфейс управления данными торговой точки
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 14:47
 */
public interface SalePointService extends SecuredRepository<SalePoint> {
}
