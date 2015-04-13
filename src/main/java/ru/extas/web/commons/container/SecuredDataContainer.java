package ru.extas.web.commons.container;

import org.apache.commons.lang3.tuple.Pair;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.security.ExtaDomain;
import ru.extas.model.security.SecuredObject;
import ru.extas.security.AbstractSecurityFilter;
import ru.extas.security.SecurityFilter;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.container.jpa.JpaLazyItemList;
import ru.extas.web.commons.container.jpa.JpaPropertyProvider;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Контейтер для данных из базы
 * <p>
 * Date: 12.09.13
 * Time: 22:50
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class SecuredDataContainer<TEntityType extends IdentifiedObject> extends ExtaDbContainer<TEntityType> {

    private final AbstractSecurityFilter<TEntityType> securityFilter;

    public SecuredDataContainer(final AbstractSecurityFilter<TEntityType> securityFilter) {
        super(securityFilter.getEntityClass());

        this.securityFilter = securityFilter;
    }

    @Override
    protected JpaLazyItemList<TEntityType> createJpaEntityItemList(Class<TEntityType> tEntityTypeClass, SupplierSer<List<Pair<String, Boolean>>> sortBySupplier, SupplierSer<Filter> filterSupplierSer, JpaPropertyProvider jpaPropertyProvider) {
        return new JpaLazyItemList<TEntityType>(tEntityTypeClass, sortBySupplier, filterSupplierSer, jpaPropertyProvider) {
            @Override
            protected List<Predicate> createContainerFilterPredicates(CriteriaBuilder cb, CriteriaQuery query, Root root) {
                final List<Predicate> predicates = super.createContainerFilterPredicates(cb, query, root);
                // Установить фильтр в соответствии с правами доступа пользователя
                final Predicate predicate = securityFilter.createSecurityPredicate(cb, query);
                if (predicate != null) {
                    predicates.add(predicate);
                    query.distinct(true);
                }
                return predicates;
            }
        };
    }

    public AbstractSecurityFilter<TEntityType> getSecurityFilter() {
        return securityFilter;
    }

    public static <TSecuredType extends SecuredObject> SecuredDataContainer<TSecuredType> create(final Class<TSecuredType> entityClass, final ExtaDomain domain) {
        return new SecuredDataContainer<TSecuredType>(new SecurityFilter<TSecuredType>(entityClass, domain));
    }
}
