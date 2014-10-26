/**
 *
 */
package ru.extas.model.common;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;

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
//@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AuditedObject extends IdentifiedObject implements Auditable<String, String> {

    /**
     * Constant <code>LOGIN_LENGTH=50</code>
     */
    public static final int LOGIN_LENGTH = 50;

    @CreatedBy
    @Column(name = "CREATED_BY", length = LOGIN_LENGTH)
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_AT")
    private DateTime createdDate;

    @LastModifiedBy
    @Column(name = "MODIFIED_BY", length = LOGIN_LENGTH)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "MODIFIED_AT")
    private DateTime lastModifiedDate;

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public DateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(DateTime creationDate) {
        this.createdDate = creationDate;
    }

    @Override
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
