package ru.extas.model.common;

import ru.extas.model.contacts.Person;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Базовый класс для всех сущностей.
 * Имплементирует ведение журналов безопасности.
 *
 * @author Valery Orlov
 *         Date: 22.03.2014
 *         Time: 17:40
 * @version $Id: $Id
 * @since 0.3.0
 */
@MappedSuperclass
public class SecuredObject extends AuditedObject {

    @ManyToMany(cascade = CascadeType.REFRESH)
    private Set<Person> associateUsers = newHashSet();

    @ElementCollection
    private Set<String> associateRegions = newHashSet();

    @ElementCollection
    private Set<String> associateBrands = newHashSet();

    /**
     * <p>Getter for the field <code>associateUsers</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<Person> getAssociateUsers() {
        return associateUsers;
    }

    /**
     * <p>Setter for the field <code>associateUsers</code>.</p>
     *
     * @param associateUsers a {@link java.util.Set} object.
     */
    public void setAssociateUsers(final Set<Person> associateUsers) {
        this.associateUsers = associateUsers;
    }

    /**
     * <p>Getter for the field <code>associateRegions</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> getAssociateRegions() {
        return associateRegions;
    }

    /**
     * <p>Setter for the field <code>associateRegions</code>.</p>
     *
     * @param associateRegions a {@link java.util.Set} object.
     */
    public void setAssociateRegions(final Set<String> associateRegions) {
        this.associateRegions = associateRegions;
    }

    /**
     * <p>Getter for the field <code>associateBrands</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> getAssociateBrands() {
        return associateBrands;
    }

    /**
     * <p>Setter for the field <code>associateBrands</code>.</p>
     *
     * @param associateBrands a {@link java.util.Set} object.
     */
    public void setAssociateBrands(final Set<String> associateBrands) {
        this.associateBrands = associateBrands;
    }
}
