package ru.extas.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.Set;

/**
 * Группа пользователей
 *
 * @author Valery Orlov
 */
@Entity
@Table(name = "USER_GROUP")
public class UserGroup extends AbstractExtaObject {

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the permissionList
     */
    public Set<String> getPermissionList() {
        return permissionList;
    }

    /**
     * @param permissionList the permissionList to set
     */
    public void setPermissionList(Set<String> permissionList) {
        this.permissionList = permissionList;
    }

}
