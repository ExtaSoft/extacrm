package ru.extas.model.security;

import ru.extas.model.common.AuditedObject;
import ru.extas.model.contacts.Person;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Совокупная информация о пользователе
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@Table(name = "USER_PROFILE")
public class UserProfile extends AuditedObject {

    private static final long serialVersionUID = 6937423190833815234L;

    // Login/email
    @Column(length = LOGIN_LENGTH)
    @Max(LOGIN_LENGTH)
    private String login;

    // Ссылка на контакт
    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
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
    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "USER_GROUP_LINK",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")})
    private Set<UserGroup> groupList = new HashSet<>();

    // Пользователь заблокирован
    private boolean blocked;

    @ElementCollection
    @CollectionTable(name = "USER_PROFILE_REGION",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")})
    private Set<String> permitRegions = newHashSet();

    @ElementCollection
    @CollectionTable(name = "USER_PROFILE_BRAND",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")})
    private Set<String> permitBrands = newHashSet();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ExtaPermission> permissions;


    /**
     * <p>Getter for the field <code>groupList</code>.</p>
     *
     * @return the groupList
     */
    public Set<UserGroup> getGroupList() {
        return groupList;
    }

    /**
     * <p>Setter for the field <code>groupList</code>.</p>
     *
     * @param groupList the groupList to set
     */
    public void setGroupList(Set<UserGroup> groupList) {
        this.groupList = groupList;
    }

    /**
     * <p>Setter for the field <code>role</code>.</p>
     *
     * @param role the role to set
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * <p>Getter for the field <code>login</code>.</p>
     *
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * <p>Setter for the field <code>login</code>.</p>
     *
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * <p>Getter for the field <code>password</code>.</p>
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * <p>Setter for the field <code>password</code>.</p>
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * <p>Getter for the field <code>role</code>.</p>
     *
     * @return a {@link ru.extas.model.security.UserRole} object.
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * <p>Getter for the field <code>contact</code>.</p>
     *
     * @return the contact
     */
    public Person getContact() {
        return contact;
    }

    /**
     * <p>Setter for the field <code>contact</code>.</p>
     *
     * @param contact the contact to set
     */
    public void setContact(Person contact) {
        this.contact = contact;
    }

    /**
     * <p>isChangePassword.</p>
     *
     * @return the changePassword
     */
    public boolean isChangePassword() {
        return changePassword;
    }

    /**
     * <p>Setter for the field <code>changePassword</code>.</p>
     *
     * @param changePassword the changePassword to set
     */
    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

    /**
     * <p>isBlocked.</p>
     *
     * @return the blocked
     */
    public boolean isBlocked() {
        return blocked;
    }

    /**
     * <p>Setter for the field <code>blocked</code>.</p>
     *
     * @param blocked the blocked to set
     */
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    /**
     * <p>Getter for the field <code>passwordSalt</code>.</p>
     *
     * @return the passwordSalt
     */
    public String getPasswordSalt() {
        return passwordSalt;
    }

    /**
     * <p>Setter for the field <code>passwordSalt</code>.</p>
     *
     * @param passwordSalt the passwordSalt to set
     */
    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
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
}
