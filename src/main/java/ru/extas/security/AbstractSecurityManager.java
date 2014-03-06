package ru.extas.security;

import ru.extas.server.UserManagementService;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Базовая/общая реализация интерфейса управления безопасностью объектов
 *
 * @author Valery Orlov
 *         Date: 06.03.14
 *         Time: 19:00
 */
public abstract class AbstractSecurityManager implements EntitySecurityManager {

	@Inject
	protected UserManagementService userService;

	@Override
	public void secureJpaQuery(final CriteriaBuilder cb, final CriteriaQuery<?> cq, final List<Predicate> predicates) {

		// Определить домен
		ExtaDomain domain = ExtaDomain.PERSON;
		// Определить область видимости и Наложить фильтр в соответствии с областью видимости
		if(userService.isPermittedTarget(domain, SecureTarget.ALL))
			;// Доступно все, ничего не делаем
		else if(userService.isPermittedTarget(domain, SecureTarget.CORPORATE)) {
			// Получаем набор пользователей для "Собственных" объектов
			final List<String> userList = userService.findCorporateUsers();
			predicates.add(createPredicateByOwners(userList, cb, cq));
		}
		else if(userService.isPermittedTarget(domain, SecureTarget.SALE_POINT)) {
			// Получаем набор пользователей для "Собственных" объектов
			final List<String> userList = userService.findSalePointUsers();
			predicates.add(createPredicateByOwners(userList, cb, cq));
		}
		else if(userService.isPermittedTarget(domain, SecureTarget.OWNONLY)) {
			// Получаем набор пользователей для "Собственных" объектов
			final List<String> userList = newArrayList(userService.getCurrentUserLogin());
			predicates.add(createPredicateByOwners(userList, cb, cq));
		}
		else
			throw new SecurityException("Forbidden access to objects!!!");// Ничего недоступно
	}
}
