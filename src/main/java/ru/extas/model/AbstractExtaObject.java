/**
 *
 */
package ru.extas.model;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;

import javax.jdo.annotations.*;
import javax.jdo.listener.StoreCallback;
import java.io.Serializable;

/**
 * Базовый класс для всех сущностей. Имплементирует ID и макеры изменений.
 *
 * @author Valery Orlov
 */
@PersistenceCapable(detachable = "true", identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class AbstractExtaObject implements StoreCallback, Serializable {

    private static final long serialVersionUID = 9098736299506726746L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String key;

    @Persistent
    private String createdBy;

    @Persistent
    private DateTime createdAt;

    @Persistent
    private String modifiedBy;

    @Persistent
    private DateTime modifiedAt;

    /**
     *
     */
    AbstractExtaObject() {
        super();
    }

    /**
     * Получить ID объекта (gae.encoded-pk)
     *
     * @return ID объекта
     */
    public String getKey() {
        return key;
    }

    /**
     * Установить ID объекта. <b> Не устанавливать для новых (вставляемых)
     * объектов!!!</b>
     *
     * @param key ID объекта
     */
    public void setKey(String key) {
        this.key = key;
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
        result = prime * result + ((key == null) ? 0 : key.hashCode());
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
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.jdo.listener.StoreCallback#jdoPreStore()
     */
    @Override
    public void jdoPreStore() {
        // Ставим маркеры создания/модификации
        Subject subject = SecurityUtils.getSubject();
        String login = (String) subject.getPrincipal();
        DateTime dt = DateTime.now();
        modifiedAt = dt;
        modifiedBy = login;
        if (key == null) {
            createdAt = dt;
            createdBy = login;
        }
    }

}