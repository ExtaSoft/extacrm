package ru.extas.server;

import ru.extas.model.Policy;

import java.util.List;

/**
 * <p>PolicyService interface.</p>
 *
 * @author Valery Orlov
 *         Date: 19.12.13
 *         Time: 12:13
 * @version $Id: $Id
 */
public interface PolicyService {
/**
 * Возвращает список всех доступных полисов
 *
 * @return список полисов
 */
List<Policy> loadAvailable();

/**
 * Забронировать полис
 *
 * @param policy бронируемый полис
 */
void bookPolicy(Policy policy);

/**
 * Реализовать полис
 *
 * @param policy реализуемый полис
 */
void issuePolicy(Policy policy);

/**
 * Забронировать полис
 *
 * @param regNum номер резервируемого полиса
 */
void bookPolicy(String regNum);

/**
 * Реализовать полис
 *
 * @param regNum номер реализуемого полиса
 */
void issuePolicy(String regNum);
}
