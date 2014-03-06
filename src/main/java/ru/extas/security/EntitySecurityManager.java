package ru.extas.security;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * Определяет методы для управления сущностями системы в разрезе безопасности (разграничения прав доступа)
 *
 * @author Valery Orlov
 *         Date: 05.03.14
 *         Time: 18:04
 */
public interface EntitySecurityManager {

	void secureJpaQuery(CriteriaBuilder cb, CriteriaQuery<?> cq, List<Predicate> predicates);

	Predicate createPredicateByOwners(List<String> userList, CriteriaBuilder cb, CriteriaQuery<?> cq);
}
