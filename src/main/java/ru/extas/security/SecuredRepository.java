package ru.extas.security;

import org.springframework.data.jpa.repository.JpaRepository;
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

    Entity secureSave(Entity entity);

    void permitAndSave(Entity entity, Person userContact, Collection<String> regions, Collection<String> brands);

    void permitAndSave(Collection<Entity> entities, Person userContact, Collection<String> regions, Collection<String> brands);

    void permitObject(Entity entity, Person userContact, Collection<String> regions, Collection<String> brands);

    void permitObject(Collection<Entity> entities, Person userContact, Collection<String> regions, Collection<String> brands);
}
