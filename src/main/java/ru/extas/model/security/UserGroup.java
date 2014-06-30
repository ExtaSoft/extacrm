package ru.extas.model.security;

import ru.extas.model.common.AuditedObject;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.Set;

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

    /**
     * Список разрешений группы
     */
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExtaPermission> permissions;

    @ManyToMany(mappedBy = "groupList")
    private Set<UserProfile> users;

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
}
