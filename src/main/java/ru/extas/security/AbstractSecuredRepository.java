package ru.extas.security;

import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.common.SecuredObject;
import ru.extas.model.contacts.Person;
import ru.extas.server.security.UserManagementService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    /** {@inheritDoc} */
    @Transactional
    @Override
    public Entity secureSave(Entity entity) {
        Person userContact = userService.getCurrentUserContact();
        return permitAndSave(entity, userContact, getObjectRegions(entity), getObjectBrands(entity));
    }

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

    /** {@inheritDoc} */
    @Transactional
    @Override
    public Entity permitAndSave(Entity entity, Person userContact, Collection<String> regions, Collection<String> brands) {
        if (entity != null) {
            // Доступ пользователя к объекту
            entity.getAssociateUsers().add(userContact);
            // Видимость объекта в разрезе регионов
            if (!isEmpty(regions))
                entity.getAssociateRegions().addAll(regions);
            // Видимость объекта в разрезе брендов
            if (!isEmpty(brands))
                entity.getAssociateBrands().addAll(brands);
            return getEntityRepository().save(entity);
        }
        return entity;
    }

    /** {@inheritDoc} */
    @Transactional
    @Override
    public List<Entity> permitAndSave(Collection<Entity> entities, Person userContact, Collection<String> regions, Collection<String> brands) {
        List<Entity> result = new ArrayList<>();
        if (!isEmpty(entities)) {
            if (!isEmpty(entities)) {
                for(Entity entity : entities)
                    result.add(permitAndSave(entity, userContact, regions, brands));
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @Transactional
    @Override
    public Entity permitAndSave(Entity entity, Person userContact) {
        if (entity != null) {
            return permitAndSave(entity, userContact, getObjectRegions(entity), getObjectBrands(entity));
        }
        return entity;
    }

}
