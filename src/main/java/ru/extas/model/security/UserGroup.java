package ru.extas.model.security;

import ru.extas.model.common.AuditedObject;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Группа пользователей
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@Table(name = "USER_GROUP")
public class UserGroup extends AuditedObject implements Cloneable{

    private static final long serialVersionUID = 4149728748291041330L;

    /**
     * Имя группы
     */
    @Column(length = 50)
    @Max(50)
    private String name;

    private String description;

    @ElementCollection
    @CollectionTable(name = "USER_GROUP_REGION",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")})
    private Set<String> permitRegions = newHashSet();

    @ElementCollection
    @CollectionTable(name = "USER_GROUP_BRAND",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")})
    private Set<String> permitBrands = newHashSet();

    /**
     * Список разрешений группы
     */
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExtaPermission> permissions;

    @ManyToMany(mappedBy = "groupList")
    private Set<UserProfile> users;

    @Override
    public UserGroup clone() {
        UserGroup clone = new UserGroup();

        clone.setName(getName());
        clone.setDescription(getDescription());
        clone.setPermitRegions(newHashSet(getPermitRegions()));
        clone.setPermitBrands(newHashSet(getPermitBrands()));
        HashSet<ExtaPermission> perms = newHashSet();
        for (ExtaPermission perm : getPermissions()) {
            ExtaPermission permission = perm.clone();
            permission.setGroup(clone);
            perms.add(permission);
        }
        clone.setPermissions(perms);

        return clone;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public Set<ExtaPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<ExtaPermission> permissions) {
        this.permissions = permissions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UserProfile> getUsers() {
        return users;
    }

    public void setUsers(Set<UserProfile> users) {
        this.users = users;
    }

    public Set<String> getPermitRegions() {
        return permitRegions;
    }

    public void setPermitRegions(Set<String> permitRegions) {
        this.permitRegions = permitRegions;
    }

    public Set<String> getPermitBrands() {
        return permitBrands;
    }

    public void setPermitBrands(Set<String> permitBrands) {
        this.permitBrands = permitBrands;
    }
}
