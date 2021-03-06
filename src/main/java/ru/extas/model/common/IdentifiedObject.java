package ru.extas.model.common;

import org.eclipse.persistence.annotations.UuidGenerator;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

import java.util.Objects;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Объект с идентификатором и контролем версий
 *
 * @author Valery Orlov
 *         Date: 22.03.2014
 *         Time: 17:16
 * @version $Id: $Id
 * @since 0.3.0
 */
@MappedSuperclass
@Access(AccessType.FIELD)
@UuidGenerator(name = "system-uuid")
public class IdentifiedObject implements Persistable<String> {

    private static final long serialVersionUID = 9098736299506726746L;
    /** Constant <code>ID_SIZE=50</code> */
    public static final int ID_SIZE = 50;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "ID", length = ID_SIZE)
    private String id;

    @Version
    private int version;

    // Архивный
    @Column(name = ArchivedObject.COLUMN_NAME)
    private boolean archived;

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(final boolean archived) {
        this.archived = archived;
    }

    /**
     * <p>Getter for the field <code>version</code>.</p>
     *
     * @return a int.
     */
    public int getVersion() {
        return version;
    }

    /**
     * <p>Setter for the field <code>version</code>.</p>
     *
     * @param version a int.
     */
    public void setVersion(final int version) {
        this.version = version;
    }

    /**
     * Получить ID объекта (uuid.encoded-pk)
     *
     * @return ID объекта
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Установить ID объекта. <b> Не устанавливать для новых (вставляемых)
     * объектов!!!</b>
     *
     * @param key ID объекта
     */
    public void setId(final String key) {
        this.id = key;
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final IdentifiedObject other = (IdentifiedObject) obj;
        if (id == null || other.id == null) {
            return super.equals(other);
        } else
            return Objects.equals(id, other.id);
    }

    @Override
    public boolean isNew() {
        return isNullOrEmpty(getId());
    }
}
