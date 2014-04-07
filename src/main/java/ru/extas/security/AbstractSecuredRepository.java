package ru.extas.security;

import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.common.SecuredObject;
import ru.extas.model.contacts.Person;
import ru.extas.server.users.UserManagementService;

import javax.inject.Inject;
import java.util.Collection;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Реализация базовых действий по распределению прав доступа на объект
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 13:08
 */
public abstract class AbstractSecuredRepository<Entity extends SecuredObject> implements SecuredRepository<Entity> {

    @Inject
    protected UserManagementService userService;

    @Transactional
    @Override
    public Entity secureSave(Entity entity) {
        Person userContact = userService.getCurrentUserContact();
        permitObject(entity, userContact, getObjectRegions(entity), getObjectBrands(entity));
        return getEntityRepository().save(entity);
    }

    protected abstract Collection<String> getObjectBrands(Entity entity);

    protected abstract Collection<String> getObjectRegions(Entity entity);

    @Transactional
    @Override
    public void permitAndSave(Entity entity, Person userContact, Collection<String> regions, Collection<String> brands) {
        if (entity != null) {
            permitObject(entity, userContact, regions, brands);
            getEntityRepository().save(entity);
        }
    }

    @Transactional
    @Override
    public void permitAndSave(Collection<Entity> entities, Person userContact, Collection<String> regions, Collection<String> brands) {
        if (!isEmpty(entities)) {
            permitObject(entities, userContact, regions, brands);
            getEntityRepository().save(entities);
        }
    }

    @Transactional
    @Override
    public void permitAndSave(Entity entity, Person userContact) {
        if (entity != null) {
            permitObject(entity, userContact, getObjectRegions(entity), getObjectBrands(entity));
            getEntityRepository().save(entity);
        }
    }

    @Transactional
    @Override
    public void permitObject(Entity entity, Person userContact, Collection<String> regions, Collection<String> brands) {
        if (entity != null) {
            // Доступ пользователя к объекту
            entity.getAssociateUsers().add(userContact);
            // Видимость объекта в разрезе регионов
            if (!isEmpty(regions))
                entity.getAssociateRegions().addAll(regions);
            // Видимость объекта в разрезе брендов
            if (!isEmpty(brands))
                entity.getAssociateBrands().addAll(brands);
        }
    }

    @Transactional
    @Override
    public void permitObject(Collection<Entity> entities, Person userContact, Collection<String> regions, Collection<String> brands) {
        if (!isEmpty(entities)) {
            for(Entity entity : entities)
                permitObject(entity, userContact, regions, brands);
        }
    }
}
