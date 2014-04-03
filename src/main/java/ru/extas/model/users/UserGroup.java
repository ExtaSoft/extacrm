package ru.extas.model.users;

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

    // Список разрешений группы
    @ElementCollection
    @CollectionTable(name = "USER_GROUP_PERMISSION")
    private Set<String> permissionList;

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
     * <p>Getter for the field <code>permissionList</code>.</p>
     *
     * @return the permissionList
     */
    public Set<String> getPermissionList() {
        return permissionList;
    }

    /**
     * <p>Setter for the field <code>permissionList</code>.</p>
     *
     * @param permissionList the permissionList to set
     */
    public void setPermissionList(Set<String> permissionList) {
        this.permissionList = permissionList;
    }

}
