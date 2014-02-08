/**
 *
 */
package ru.extas.model;

import org.eclipse.persistence.annotations.UuidGenerator;
import org.joda.time.DateTime;
import ru.extas.server.UserManagementService;

import javax.persistence.*;
import java.io.Serializable;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Базовый класс для всех сущностей. Имплементирует ID и макеры изменений.
 *
 * @author Valery Orlov
 */
@MappedSuperclass
@Access(AccessType.FIELD)
@UuidGenerator(name = "system-uuid")
public abstract class AbstractExtaObject implements Serializable {

	private static final long serialVersionUID = 9098736299506726746L;
	public static final int LOGIN_LENGTH = 50;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "ID", length = 50)
	protected String id;

	@Column(name = "CREATED_BY", length = LOGIN_LENGTH)
	protected String createdBy;

	@Column(name = "CREATED_AT")
	protected DateTime createdAt;

	@Column(name = "MODIFIED_BY", length = LOGIN_LENGTH)
	protected String modifiedBy;

	@Column(name = "MODIFIED_AT")
	protected DateTime modifiedAt;

	@Version
	protected int version;

	/**
	 *
	 */
	AbstractExtaObject() {
		super();
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	/**
	 * Получить ID объекта (uuid.encoded-pk)
	 *
	 * @return ID объекта
	 */
	public String getId() {
		return id;
	}

	/**
	 * Установить ID объекта. <b> Не устанавливать для новых (вставляемых)
	 * объектов!!!</b>
	 *
	 * @param key ID объекта
	 */
	public void setId(String key) {
		this.id = key;
	}

	/**
	 * Получить логин пользователя создавшего объект
	 *
	 * @return идентификатор пользователя
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Получить время создания объекта
	 *
	 * @return время создания
	 */
	public DateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Получить логин пользователя изменившего объект
	 *
	 * @return идентификатор пользователя
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * Получить время изменения объекта
	 *
	 * @return время изменения
	 */
	public DateTime getModifiedAt() {
		return modifiedAt;
	}

	/**
	 * @param modifiedAt the modifiedAt to set
	 */
	public void setModifiedAt(DateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result;
		if (id != null) {
			result = 1;
			final int prime = 31;
			result = prime * result + id.hashCode();
		} else {
			result = super.hashCode();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractExtaObject other = (AbstractExtaObject) obj;
		if (id == null || other.id == null) {
			return super.equals(other);
		} else
			return id.equals(other.id);
	}

	@PrePersist
	private void markCreating() {
		// Ставим маркеры создания/модификации
		if (createdAt == null && createdBy == null) {
			UserManagementService userManagementService = lookup(UserManagementService.class);
			String login = userManagementService.getCurrentUserLogin();
			DateTime dt = DateTime.now();
			createdAt = dt;
			createdBy = login;
		}
		if (modifiedAt == null && modifiedBy == null) {
			modifiedAt = createdAt;
			modifiedBy = createdBy;
		}
	}

	@PreUpdate
	private void markUpdating() {
		// Ставим маркеры создания/модификации
		UserManagementService userManagementService = lookup(UserManagementService.class);
		String login = userManagementService.getCurrentUserLogin();
		modifiedAt = DateTime.now();
		modifiedBy = login;
	}
}