package ru.extas.model.common;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
public abstract class FileContainer extends AuditedObject {

    /** Constant <code>OWNER_ID_COLUMN="OWNER_ID"</code> */
    public static final String OWNER_ID_COLUMN = "OWNER_ID";

    // Id объекта к которому привязан файл
    @Column(name = OWNER_ID_COLUMN, length = ID_SIZE)
    private String ownerId;

    // Описание файла
    @Column
    @Size(max = 255)
    private String description;

    // Имя файла
    @Column
    @Size(max = 255)
    private String name;

    // Тип
    @Column
    @Size(max = 255)
    private String mimeType;

    // Размер
    @Column
    private long fileSize;

    // Данные файла
    @Lob
    @Basic(fetch= FetchType.LAZY)
    private byte[] fileData;

    /**
     * Копирующий конструктор
     * @param file - исходный объект
     */
    public FileContainer(String ownerId, FileContainer file) {
        this.ownerId = ownerId;
        description = file.getDescription();
        name = file.getName();
        mimeType = file.getMimeType();
        fileSize = file.getFileSize();
        fileData = Arrays.copyOf(file.getFileData(), file.getFileData().length);
    }

    public FileContainer() {
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

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>fileData</code>.</p>
     *
     * @return an array of byte.
     */
    public byte[] getFileData() {
        return fileData;
    }

    /**
     * <p>Setter for the field <code>fileData</code>.</p>
     *
     * @param fileData an array of byte.
     */
    public void setFileData(final byte[] fileData) {
        this.fileData = fileData;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param description a {@link java.lang.String} object.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * <p>Getter for the field <code>mimeType</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * <p>Setter for the field <code>mimeType</code>.</p>
     *
     * @param mimeType a {@link java.lang.String} object.
     */
    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * <p>Getter for the field <code>fileSize</code>.</p>
     *
     * @return a long.
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * <p>Setter for the field <code>fileSize</code>.</p>
     *
     * @param fileSize a long.
     */
    public void setFileSize(final long fileSize) {
        this.fileSize = fileSize;
    }
}
