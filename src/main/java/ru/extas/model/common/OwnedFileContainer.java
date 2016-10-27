package ru.extas.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.Arrays;

/**
 * Модель данных для хранения файлов прикрепляемых к объектам системы
 *
 * @author Valery Orlov
 *         Date: 17.04.2014
 *         Time: 23:15
 * @version $Id: $Id
 * @since 0.4.2
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class OwnedFileContainer extends FileContainer {

    /** Constant <code>OWNER_ID_COLUMN="OWNER_ID"</code> */
    public static final String OWNER_ID_COLUMN = "OWNER_ID";

    // Id объекта к которому привязан файл
    @Column(name = OWNER_ID_COLUMN, length = ID_SIZE)
    private String ownerId;

    /**
     * Копирующий конструктор
     * @param file - исходный объект
     */
    public OwnedFileContainer(final String ownerId, final FileContainer file) {
        this.ownerId = ownerId;
        description = file.getDescription();
        name = file.getName();
        mimeType = file.getMimeType();
        fileSize = file.getFileSize();
        fileData = Arrays.copyOf(file.getFileData(), file.getFileData().length);
    }

    public OwnedFileContainer() {
    }

    /**
     * <p>Getter for the field <code>ownerId</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * <p>Setter for the field <code>ownerId</code>.</p>
     *
     * @param parentId a {@link java.lang.String} object.
     */
    public void setOwnerId(final String parentId) {
        this.ownerId = parentId;
    }

}
