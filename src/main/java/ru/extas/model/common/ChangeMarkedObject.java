/**
 *
 */
package ru.extas.model.common;

import org.joda.time.DateTime;
import ru.extas.server.users.UserManagementService;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Базовый класс для всех сущностей.
 * Имплементирует макеры изменений.
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@MappedSuperclass
public abstract class ChangeMarkedObject extends IdentifiedObject {

    /**
     * Constant <code>LOGIN_LENGTH=50</code>
     */
    public static final int LOGIN_LENGTH = 50;

    @Column(name = "CREATED_BY", length = LOGIN_LENGTH)
    protected String createdBy;

    @Column(name = "CREATED_AT")
    protected DateTime createdAt;

    @Column(name = "MODIFIED_BY", length = LOGIN_LENGTH)
    protected String modifiedBy;

    @Column(name = "MODIFIED_AT")
    protected DateTime modifiedAt;

    /**
     *
     */
    protected ChangeMarkedObject() {
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
