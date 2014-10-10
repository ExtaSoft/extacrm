package ru.extas.security;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.security.AccessRole;
import ru.extas.model.security.SecuredObject;
import ru.extas.model.contacts.Person;
import ru.extas.server.security.UserManagementService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Реализация базовых действий по распределению прав доступа на объект
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 13:08
 * @version $Id: $Id
 * @since 0.3.0
 */
public abstract class AbstractSecuredRepository<Entity extends SecuredObject> implements SecuredRepository<Entity> {

    @Inject
    protected UserManagementService userService;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Entity secureSave(Entity entity) {
        return permitAndSave(entity, getObjectUsers(entity), getObjectSalePoints(entity), getObjectCompanies(entity), getObjectRegions(entity), getObjectBrands(entity));
    }

    protected abstract Collection<Pair<Person, AccessRole>> getObjectUsers(Entity entity);

    protected Pair<Person, AccessRole> getCurUserAccess(Entity entity) {
        Person currentUserContact = userService.getCurrentUserContact();
        return new ImmutablePair<>(currentUserContact, entity.isNew() ? AccessRole.OWNER : AccessRole.EDITOR);
    }

    protected Collection<Pair<Person, AccessRole>> reassigneRole(Collection<Pair<Person, AccessRole>> users, AccessRole role) {
        List<Pair<Person, AccessRole>> newUsers = newArrayListWithCapacity(users.size());

        users.forEach(p -> newUsers.add(new ImmutablePair<>(p.getLeft(), role)));

        return newUsers;
    }

    protected abstract Collection<Company> getObjectCompanies(Entity entity);

    protected abstract Collection<SalePoint> getObjectSalePoints(Entity entity);

    /**
     * <p>getObjectBrands.</p>
     *
     * @param entity a Entity object.
     * @return a {@link java.util.Collection} object.
     */
    protected abstract Collection<String> getObjectBrands(Entity entity);

    /**
     * <p>getObjectRegions.</p>
     *
     * @param entity a Entity object.
     * @return a {@link java.util.Collection} object.
     */
    protected abstract Collection<String> getObjectRegions(Entity entity);

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Entity permitAndSave(Entity entity,
                                Collection<Pair<Person, AccessRole>> users,
                                Collection<SalePoint> salePoints,
                                Collection<Company> companies,
                                Collection<String> regions,
                                Collection<String> brands) {
        if (entity != null) {
            // Доступ пользователей к объекту
            entity.addSecurityUserAccess(users);
            // Видимость объекта в разрезе Торговых точек
            entity.addSecuritySalePoints(salePoints);
            // Видимость объекта в разрезе Компаний
            entity.addSecurityCompanies(companies);
            // Видимость объекта в разрезе регионов
            entity.addSecurityRegions(regions);
            // Видимость объекта в разрезе брендов
            entity.addSecurityBrands(brands);
            return getEntityRepository().save(entity);
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public List<Entity> permitAndSave(Collection<Entity> entities,
                                      Collection<Pair<Person, AccessRole>> users,
                                      Collection<SalePoint> salePoints,
                                      Collection<Company> companies,
                                      Collection<String> regions,
                                      Collection<String> brands) {
        List<Entity> result = new ArrayList<>();
        if (!isEmpty(entities)) {
            if (!isEmpty(entities)) {
                for (Entity entity : entities)
                    result.add(permitAndSave(entity, users, salePoints, companies, regions, brands));
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Entity permitAndSave(Entity entity, Pair<Person, AccessRole> user) {
        if (entity != null) {
            final ArrayList<Pair<Person, AccessRole>> users = newArrayList(user);
            users.addAll(getObjectUsers(entity));
            return permitAndSave(entity, users, getObjectSalePoints(entity), getObjectCompanies(entity), getObjectRegions(entity), getObjectBrands(entity));
        }
        return entity;
    }

}
