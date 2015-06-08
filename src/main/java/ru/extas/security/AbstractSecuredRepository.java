package ru.extas.security;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.persistence.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.Employee;
import ru.extas.model.security.AccessRole;
import ru.extas.model.security.SecuredObject;
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

    private final static Logger logger = LoggerFactory.getLogger(AbstractSecuredRepository.class);

    @Inject
    protected UserManagementService userService;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Entity secureSave(final Entity entity) {
        return permitAndSave(entity, getObjectUsers(entity), getObjectSalePoints(entity), getObjectCompanies(entity), getObjectRegions(entity), getObjectBrands(entity));
    }

    protected abstract Collection<Pair<String, AccessRole>> getObjectUsers(Entity entity);

    protected Pair<String, AccessRole> getCurUserAccess(final Entity entity) {
        final Employee currentUserContact;
        if (userService.isUserAuthenticated())
            currentUserContact = userService.getCurrentUserEmployee();
        else
            currentUserContact = userService.findUserEmployeeByLogin("admin");

        return new ImmutablePair<>(currentUserContact.getId(), entity.isNew() ? AccessRole.OWNER : AccessRole.EDITOR);
    }

    protected Collection<Pair<String, AccessRole>> reassigneRole(final Collection<Pair<String, AccessRole>> users, final AccessRole role) {
        final List<Pair<String, AccessRole>> newUsers = newArrayListWithCapacity(users.size());

        users.forEach(p -> newUsers.add(new ImmutablePair<>(p.getLeft(), role)));

        return newUsers;
    }

    protected abstract Collection<String> getObjectCompanies(Entity entity);

    protected abstract Collection<String> getObjectSalePoints(Entity entity);

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
    public Entity permitAndSave(final Entity entity,
                                final Collection<Pair<String, AccessRole>> users,
                                final Collection<String> salePoints,
                                final Collection<String> companies,
                                final Collection<String> regions,
                                final Collection<String> brands) {
        if (entity != null) {
            logger.info("BEGIN Base Secure Save (BSS): Type - {}; Entity ID - {}", entity.getClass().getSimpleName(), entity.getId());
            // Доступ пользователей к объекту
            skipELValidationException(() -> entity.addSecurityUserAccess(users));
            // Видимость объекта в разрезе Торговых точек
            skipELValidationException(() -> entity.addSecuritySalePoints(salePoints));
            // Видимость объекта в разрезе Компаний
            skipELValidationException(() -> entity.addSecurityCompanies(companies));
            // Видимость объекта в разрезе регионов
            skipELValidationException(() -> entity.addSecurityRegions(regions));
            // Видимость объекта в разрезе брендов
            skipELValidationException(() -> entity.addSecurityBrands(brands));
            final Entity save = getEntityRepository().save(entity);
            logger.info("END Base Secure Save (BSS): Type - {}; Entity ID - {}", entity.getClass().getSimpleName(), entity.getId());
            return save;
        }
        return entity;
    }

    private void skipELValidationException(final Runnable runnable) {
        try {
            runnable.run();
        } catch (ValidationException e) {
            logger.warn("EclipseLink ValidationException skipped!!!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public List<Entity> permitAndSave(final Collection<Entity> entities,
                                      final Collection<Pair<String, AccessRole>> users,
                                      final Collection<String> salePoints,
                                      final Collection<String> companies,
                                      final Collection<String> regions,
                                      final Collection<String> brands) {
        final List<Entity> result = new ArrayList<>();
        if (!isEmpty(entities)) {
            if (!isEmpty(entities)) {
                for (final Entity entity : entities)
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
    public Entity permitAndSave(final Entity entity, final Pair<String, AccessRole> user) {
        if (entity != null) {
            final ArrayList<Pair<String, AccessRole>> users = newArrayList(user);
            users.addAll(getObjectUsers(entity));
            return permitAndSave(entity, users, getObjectSalePoints(entity), getObjectCompanies(entity), getObjectRegions(entity), getObjectBrands(entity));
        }
        return entity;
    }

}
