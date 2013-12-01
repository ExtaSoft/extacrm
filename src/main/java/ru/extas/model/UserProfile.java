package ru.extas.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.HashSet;
import java.util.Set;

/**
 * Совокупная информация о пользователе
 *
 * @author Valery Orlov
 */
@Entity
@Table(name = "USER_PROFILE")
public class UserProfile extends AbstractExtaObject {

    private static final long serialVersionUID = 6937423190833815234L;

    // Login/email
    @Column(length = LOGIN_LENGTH)
    @Max(LOGIN_LENGTH)
    private String login;

    // Ссылка на контакт
    @OneToOne
    private Person contact;

    // Password (hash)
    private String password;

    // Ключ шифрования пароля
    @Column(name = "PASSWORD_SALT", length = 30)
    private String passwordSalt;

    // Требование сменить пароль при следующем входе
    @Column(name = "CHANGE_PASSWORD", length = 50)
    private boolean changePassword;

    // Основная роль пользователя
    private UserRole role;

    // Группы в которых состоит пользователь
    @ManyToMany
    @JoinTable(name = "USER_GROUP_LINK")
    private Set<UserGroup> groupList = new HashSet<>();

    // Пользователь заблокирован
    private boolean blocked;

    /**
     * @return the groupList
     */
    public Set<UserGroup> getGroupList() {
        return groupList;
    }

    /**
     * @param groupList the groupList to set
     */
    public void setGroupList(Set<UserGroup> groupList) {
        this.groupList = groupList;
    }

    /**
     * @param role the role to set
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    /**
     * @return the contact
     */
    public Person getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(Person contact) {
        this.contact = contact;
    }

    /**
     * @return the changePassword
     */
    public boolean isChangePassword() {
        return changePassword;
    }

    /**
     * @param changePassword the changePassword to set
     */
    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

    /**
     * @return the blocked
     */
    public boolean isBlocked() {
        return blocked;
    }

    /**
     * @param blocked the blocked to set
     */
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    /**
     * @return the passwordSalt
     */
    public String getPasswordSalt() {
        return passwordSalt;
    }

    /**
     * @param passwordSalt the passwordSalt to set
     */
    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

}
