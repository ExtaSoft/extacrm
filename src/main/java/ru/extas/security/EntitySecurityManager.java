package ru.extas.security;

import ru.extas.model.common.SecuredObject;

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
 * @version $Id: $Id
 * @since 0.3
 */
public interface EntitySecurityManager {

    /**
     * <p>secureJpaQuery.</p>
     *
     * @param cb         a {@link javax.persistence.criteria.CriteriaBuilder} object.
     * @param cq         a {@link javax.persistence.criteria.CriteriaQuery} object.
     * @param predicates a {@link java.util.List} object.
     */
    void secureJpaQuery(CriteriaBuilder cb, CriteriaQuery<?> cq, List<Predicate> predicates);

    /**
     * <p>createPredicateByOwners.</p>
     *
     * @param userList a {@link java.util.List} object.
     * @param cb       a {@link javax.persistence.criteria.CriteriaBuilder} object.
     * @param cq       a {@link javax.persistence.criteria.CriteriaQuery} object.
     * @return a {@link javax.persistence.criteria.Predicate} object.
     */
    Predicate createPredicateByOwners(List<String> userList, CriteriaBuilder cb, CriteriaQuery<?> cq);

    /**
     * Записать текущему пользователю привилегии владельца на объект
     *
     * @param securedObject объект на которых распространяются привилегии
     */
    void addOwnerPrivileges(SecuredObject securedObject);
}
