package ru.extas.model;

import java.io.Serializable;
import java.util.Set;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Совокупная информация о пользователе
 * 
 * @author Valery Orlov
 * 
 */
@PersistenceCapable(detachable = "true", identityType = IdentityType.DATASTORE)
public class UserProfile implements Serializable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String key;

	// Login/email
	@Persistent
	private String login;

	// Password (hash)
	@Persistent
	private String password;

	// Основная роль пользователя
	@Persistent
	private String role;

	// Группы в которых состоит пользователь
	@Persistent
	private Set<UserGroup> groupList;

	/**
	 * @return the groupList
	 */
	public Set<UserGroup> getGroupList() {
		return groupList;
	}

	/**
	 * @param groupList
	 *            the groupList to set
	 */
	public void setGroupList(Set<UserGroup> groupList) {
		this.groupList = groupList;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the key
	 */
	public final String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public final void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the login
	 */
	public final String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public final void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public final String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public final void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

}
