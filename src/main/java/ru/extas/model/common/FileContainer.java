package ru.extas.model.common;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Created by valery on 26.10.16.
 */
@MappedSuperclass
public abstract class FileContainer extends AuditedObject {

    // Описание файла
    @Column
    @Size(max = 255)
    protected String description;
    // Имя файла
    @Column
    @Size(max = 255)
    protected String name;
    // Тип
    @Column
    @Size(max = 255)
    protected String mimeType;
    // Размер
    @Column
    protected long fileSize;
    // Данные файла
    @Lob
    @Basic(fetch= FetchType.LAZY)
    protected byte[] fileData;

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link String} object.
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
     * @return a {@link String} object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param description a {@link String} object.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * <p>Getter for the field <code>mimeType</code>.</p>
     *
     * @return a {@link String} object.
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * <p>Setter for the field <code>mimeType</code>.</p>
     *
     * @param mimeType a {@link String} object.
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
