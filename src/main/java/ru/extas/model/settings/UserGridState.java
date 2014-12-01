package ru.extas.model.settings;

import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.security.UserProfile;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Профиль таблицы (порядок, размер, видимость колонок)
 *
 * @author Valery Orlov
 *         Date: 01.12.2014
 *         Time: 0:55
 */
@Entity
@Table(name = "USER_GRID_STATE", indexes = {@Index(columnList = "USER_ID, TABLE_ID, NAME")})
public class UserGridState extends IdentifiedObject {

    @OneToOne(fetch = FetchType.LAZY)
    private UserProfile user;

    // ru.extas.web.contacts.company.CompaniesGrid
    @Column(name = "TABLE_ID", length = 256)
    @Size(max = 256)
    private String tableId;

    @Column(length = 50)
    @Size(max = 50)
    private String name;

    @Column(name = "IS_DEFAULT")
    private boolean defaultState;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String state;

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String table) {
        this.tableId = table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefaultState() {
        return defaultState;
    }

    public void setDefaultState(boolean defaultState) {
        this.defaultState = defaultState;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
