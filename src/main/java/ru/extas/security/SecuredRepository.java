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
 * @version $Id: $Id
 * @since 0.3.0
 */
public interface SecuredRepository<Entity extends SecuredObject> {

    /**
     * <p>getEntityRepository.</p>
     *
     * @return a {@link org.springframework.data.jpa.repository.JpaRepository} object.
     */
    JpaRepository<Entity, ?> getEntityRepository();

    /**
     * <p>secureSave.</p>
     *
     * @param entity a Entity object.
     * @return a Entity object.
     */
    @Transactional
    Entity secureSave(Entity entity);

    /**
     * <p>permitAndSave.</p>
     *
     * @param entity a Entity object.
     * @param userContact a {@link ru.extas.model.contacts.Person} object.
     * @param regions a {@link java.util.Collection} object.
     * @param brands a {@link java.util.Collection} object.
     */
    @Transactional
    void permitAndSave(Entity entity, Person userContact, Collection<String> regions, Collection<String> brands);

    /**
     * <p>permitAndSave.</p>
     *
     * @param entities a {@link java.util.Collection} object.
     * @param userContact a {@link ru.extas.model.contacts.Person} object.
     * @param regions a {@link java.util.Collection} object.
     * @param brands a {@link java.util.Collection} object.
     */
    @Transactional
    void permitAndSave(Collection<Entity> entities, Person userContact, Collection<String> regions, Collection<String> brands);

    /**
     * <p>permitAndSave.</p>
     *
     * @param entity a Entity object.
     * @param userContact a {@link ru.extas.model.contacts.Person} object.
     */
    @Transactional
    void permitAndSave(Entity entity, Person userContact);

    /**
     * <p>permitObject.</p>
     *
     * @param entity a Entity object.
     * @param userContact a {@link ru.extas.model.contacts.Person} object.
     * @param regions a {@link java.util.Collection} object.
     * @param brands a {@link java.util.Collection} object.
     */
    @Transactional
    void permitObject(Entity entity, Person userContact, Collection<String> regions, Collection<String> brands);

    /**
     * <p>permitObject.</p>
     *
     * @param entities a {@link java.util.Collection} object.
     * @param userContact a {@link ru.extas.model.contacts.Person} object.
     * @param regions a {@link java.util.Collection} object.
     * @param brands a {@link java.util.Collection} object.
     */
    @Transactional
    void permitObject(Collection<Entity> entities, Person userContact, Collection<String> regions, Collection<String> brands);
}
