package ru.extas.model.security;

import ru.extas.model.common.AuditedObject;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.SalePoint;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.collect.Sets.newLinkedHashSet;

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
    @Size(max = LOGIN_LENGTH)
    private String login;

    @ElementCollection
    @CollectionTable(name = "USER_LOGIN_ALIAS",
            joinColumns = {@JoinColumn(name = "USER_PROFILE_ID")},
            indexes = {
                    @Index(columnList = "ALIAS", unique = true),
                    @Index(columnList = "USER_PROFILE_ID, ALIAS", unique = true)})
    @Column(name = "ALIAS", length = LOGIN_LENGTH)
    @OrderBy("ALIAS ASC")
    private Set<String> aliases = newHashSet();

    // Ссылка на контакт
    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private Employee employee;

    // Password (hash)
    @Size(max = 255)
    private String password;

    // Ключ шифрования пароля
    @Column(name = "PASSWORD_SALT", length = 30)
    @Size(max = 30)
    private String passwordSalt;

    // Требование сменить пароль при следующем входе
    @Column(name = "CHANGE_PASSWORD")
    private boolean changePassword;

    // Основная роль пользователя
    private UserRole role;

    // Группы в которых состоит пользователь
    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "USER_GROUP_LINK",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")})
    @OrderBy("name ASC")
    private Set<UserGroup> groupList = newHashSet();

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
    @OrderBy("domain ASC")
    Set<ExtaPermission> permissions;

    // Торговые точки к которым пользователь имеет доступ
    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "USER_SALE_POINT",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "SALE_POINT_ID", referencedColumnName = "ID")})
    @OrderBy("name ASC")
    private Set<SalePoint> salePoints = newLinkedHashSet();

    public Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(final Set<String> aliases) {
        this.aliases = aliases;
    }

    public Set<SalePoint> getSalePoints() {
        return salePoints;
    }

    public void setSalePoints(final Set<SalePoint> salePoints) {
        this.salePoints = salePoints;
    }

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
    public void setGroupList(final Set<UserGroup> groupList) {
        this.groupList = groupList;
    }

    /**
     * <p>Setter for the field <code>role</code>.</p>
     *
     * @param role the role to set
     */
    public void setRole(final UserRole role) {
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
    public void setLogin(final String login) {
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
    public void setPassword(final String password) {
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
    public Employee getEmployee() {
        return employee;
    }

    /**
     * <p>Setter for the field <code>contact</code>.</p>
     *
     * @param employee the contact to set
     */
    public void setEmployee(final Employee employee) {
        this.employee = employee;
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
    public void setChangePassword(final boolean changePassword) {
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
    public void setBlocked(final boolean blocked) {
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
    public void setPasswordSalt(final String passwordSalt) {
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
    public void setPermitRegions(final Set<String> permitRegions) {
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
    public void setPermitBrands(final Set<String> permitBrands) {
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
    public void setPermissions(final Set<ExtaPermission> permissions) {
        this.permissions = permissions;
    }
}
