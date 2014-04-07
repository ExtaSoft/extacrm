package ru.extas.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.common.SecuredObject;
import ru.extas.model.contacts.Person;

import java.util.Collection;

/**
 * Интерфейс распределения прав доступа на объект
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 13:45
 */
public interface SecuredRepository<Entity extends SecuredObject> {

    JpaRepository<Entity, ?> getEntityRepository();

    @Transactional
    Entity secureSave(Entity entity);

    @Transactional
    void permitAndSave(Entity entity, Person userContact, Collection<String> regions, Collection<String> brands);

    @Transactional
    void permitAndSave(Collection<Entity> entities, Person userContact, Collection<String> regions, Collection<String> brands);

    @Transactional
    void permitAndSave(Entity entity, Person userContact);

    @Transactional
    void permitObject(Entity entity, Person userContact, Collection<String> regions, Collection<String> brands);

    @Transactional
    void permitObject(Collection<Entity> entities, Person userContact, Collection<String> regions, Collection<String> brands);
}
