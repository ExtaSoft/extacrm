package ru.extas.web.commons.container;

import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.security.ExtaDomain;
import ru.extas.model.security.SecuredObject;
import ru.extas.security.AbstractSecurityFilter;
import ru.extas.security.SecurityFilter;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.container.jpa.JpaLazyItemList;
import ru.extas.web.commons.container.jpa.JpaPropertyProvider;
import ru.extas.web.commons.container.jpa.JpaSortBy;

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
    protected JpaLazyItemList<TEntityType> createJpaEntityItemList(final Class<TEntityType> tEntityTypeClass, final SupplierSer<List<JpaSortBy>> sortBySupplier, final SupplierSer<Filter> filterSupplierSer, final JpaPropertyProvider jpaPropertyProvider) {
        return new JpaLazyItemList<TEntityType>(tEntityTypeClass, sortBySupplier, filterSupplierSer, jpaPropertyProvider) {
            @Override
            protected List<Predicate> createContainerFilterPredicates(final CriteriaBuilder cb, final CriteriaQuery query, final Root root) {
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
