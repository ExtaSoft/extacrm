/**
 *
 */
package ru.extas.model.common;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Базовый класс для всех сущностей.
 * Имплементирует макеры изменений.
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@MappedSuperclass
public abstract class AuditedObject extends IdentifiedObject {

    /**
     * Constant <code>LOGIN_LENGTH=50</code>
     */
    public static final int LOGIN_LENGTH = 50;

    @CreatedBy
    @Column(name = "CREATED_BY", length = LOGIN_LENGTH)
    protected String createdBy;

    @CreatedDate
    @Column(name = "CREATED_AT")
    protected DateTime createdAt;

    @LastModifiedBy
    @Column(name = "MODIFIED_BY", length = LOGIN_LENGTH)
    protected String modifiedBy;

    @LastModifiedDate
    @Column(name = "MODIFIED_AT")
    protected DateTime modifiedAt;

    /**
     *
     */
    protected AuditedObject() {
        super();
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
     * <p>Setter for the field <code>createdBy</code>.</p>
     *
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
     * <p>Setter for the field <code>createdAt</code>.</p>
     *
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
     * <p>Setter for the field <code>modifiedBy</code>.</p>
     *
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
     * <p>Setter for the field <code>modifiedAt</code>.</p>
     *
     * @param modifiedAt the modifiedAt to set
     */
    public void setModifiedAt(DateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

}
