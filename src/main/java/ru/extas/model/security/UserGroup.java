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
public class UserGroup extends AuditedObject {

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
    private Set<ExtaPermission> permissions = newHashSet();

    @ManyToMany(mappedBy = "groupList", cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private Set<UserProfile> users = newHashSet();

    public UserGroup createCopy() {
        UserGroup clone = new UserGroup();

        clone.setName(getName());
        clone.setDescription(getDescription());
        clone.setPermitRegions(newHashSet(getPermitRegions()));
        clone.setPermitBrands(newHashSet(getPermitBrands()));
        HashSet<ExtaPermission> perms = newHashSet();
        for (ExtaPermission perm : getPermissions()) {
            ExtaPermission permission = perm.createCopy();
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

    /**
     * <p>Getter for the field <code>permissions</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<ExtaPermission> getPermissions() {
        return permissions;
    }

    /**
     * <p>Setter for the field <code>permissions</code>.</p>
     *
     * @param permissions a {@link java.util.Set} object.
     */
    public void setPermissions(Set<ExtaPermission> permissions) {
        this.permissions = permissions;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param description a {@link java.lang.String} object.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>Getter for the field <code>users</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<UserProfile> getUsers() {
        return users;
    }

    /**
     * <p>Setter for the field <code>users</code>.</p>
     *
     * @param users a {@link java.util.Set} object.
     */
    public void setUsers(Set<UserProfile> users) {
        this.users = users;
    }

    /**
     * <p>Getter for the field <code>permitRegions</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> getPermitRegions() {
        return permitRegions;
    }

    /**
     * <p>Setter for the field <code>permitRegions</code>.</p>
     *
     * @param permitRegions a {@link java.util.Set} object.
     */
    public void setPermitRegions(Set<String> permitRegions) {
        this.permitRegions = permitRegions;
    }

    /**
     * <p>Getter for the field <code>permitBrands</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> getPermitBrands() {
        return permitBrands;
    }

    /**
     * <p>Setter for the field <code>permitBrands</code>.</p>
     *
     * @param permitBrands a {@link java.util.Set} object.
     */
    public void setPermitBrands(Set<String> permitBrands) {
        this.permitBrands = permitBrands;
    }
}
