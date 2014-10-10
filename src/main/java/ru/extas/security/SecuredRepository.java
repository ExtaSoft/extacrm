package ru.extas.security;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.security.AccessRole;
import ru.extas.model.security.SecuredObject;
import ru.extas.model.contacts.Person;

import java.util.Collection;
import java.util.List;

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
     *
     * @param entity
     * @param users
     * @param salePoints
     * @param companies
     * @param regions
     * @param brands
     * @return
     */
    @Transactional
    Entity permitAndSave(Entity entity,
                         Collection<Pair<Person, AccessRole>> users,
                         Collection<SalePoint> salePoints,
                         Collection<Company> companies,
                         Collection<String> regions,
                         Collection<String> brands);

    /**
     *
     * @param entities
     * @param users
     * @param salePoints
     * @param companies
     * @param regions
     * @param brands
     * @return
     */
    @Transactional
    List<Entity> permitAndSave(Collection<Entity> entities,
                               Collection<Pair<Person, AccessRole>> users,
                               Collection<SalePoint> salePoints,
                               Collection<Company> companies,
                               Collection<String> regions,
                               Collection<String> brands);

    /**
     * <p>permitAndSave.</p>
     *
     *  @param entity a Entity object.
     * @param userContact a {@link ru.extas.model.contacts.Person} object.
     * @return a Entity object.
     */
    @Transactional
    Entity permitAndSave(Entity entity, Pair<Person, AccessRole> user);

}
