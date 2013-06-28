package ru.extas.model;

import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Группа пользователей
 * 
 * @author Valery Orlov
 * 
 */
@PersistenceCapable(detachable = "true")
public class UserGroup extends AbstractExtaObject {

	private static final long serialVersionUID = 4149728748291041330L;

	/**
	 * Имя группы
	 */
	@Persistent
	private String name;

	// Список разрешений группы
	@Persistent
	private List<String> permissionList;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the permissionList
	 */
	public List<String> getPermissionList() {
		return permissionList;
	}

	/**
	 * @param permissionList
	 *            the permissionList to set
	 */
	public void setPermissionList(List<String> permissionList) {
		this.permissionList = permissionList;
	}

}
