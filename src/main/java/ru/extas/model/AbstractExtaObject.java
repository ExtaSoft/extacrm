/**
 *
 */
package ru.extas.model;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.eclipse.persistence.annotations.UuidGenerator;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

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
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "ID")
    @Size(max = 46)
    protected String id;
    @Column(name = "CREATED_BY")
    protected String createdBy;
    @Column(name = "CREATED_AT")
    protected DateTime createdAt;
    @Column(name = "MODIFIED_BY")
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
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @PrePersist
    private void markCreating() {
        // Ставим маркеры создания/модификации
        if (createdAt == null && createdBy == null) {
            Subject subject = SecurityUtils.getSubject();
            String login = (String) subject.getPrincipal();
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
        Subject subject = SecurityUtils.getSubject();
        String login = (String) subject.getPrincipal();
        modifiedAt = DateTime.now();
        modifiedBy = login;
    }
}