package ru.extas.model.common;

import ru.extas.model.contacts.Person;
import ru.extas.server.users.UserManagementService;

import javax.persistence.*;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Базовый класс для всех сущностей.
 * Имплементирует ведение журналов безопасности.
 *
 * @author Valery Orlov
 *         Date: 22.03.2014
 *         Time: 17:40
 */
@MappedSuperclass
public class SecuredObject extends ChangeMarkedObject {

    @ManyToMany
    private Set<Person> associateUsers = newHashSet();

    @ElementCollection
    private Set<String> associateRegions = newHashSet();

    @ElementCollection
    private Set<String> associateBrands = newHashSet();

    @PostPersist
    @PostUpdate
    protected void logSecurePrivileges() {
        UserManagementService userService = lookup(UserManagementService.class);
        getAssociateUsers().add(userService.getCurrentUserContact());
    }

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
