package ru.extas.model.common;

import ru.extas.model.contacts.Person;

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
 */
@MappedSuperclass
public class SecuredObject extends AuditedObject {

    @ManyToMany
    private Set<Person> associateUsers = newHashSet();

    @ElementCollection
    private Set<String> associateRegions = newHashSet();

    @ElementCollection
    private Set<String> associateBrands = newHashSet();

    public Set<Person> getAssociateUsers() {
        return associateUsers;
    }

    public void setAssociateUsers(Set<Person> associateUsers) {
        this.associateUsers = associateUsers;
    }

    public Set<String> getAssociateRegions() {
        return associateRegions;
    }

    public void setAssociateRegions(Set<String> associateRegions) {
        this.associateRegions = associateRegions;
    }

    public Set<String> getAssociateBrands() {
        return associateBrands;
    }

    public void setAssociateBrands(Set<String> associateBrands) {
        this.associateBrands = associateBrands;
    }
}
