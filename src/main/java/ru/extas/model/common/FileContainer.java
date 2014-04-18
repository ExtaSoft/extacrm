package ru.extas.model.common;

import javax.persistence.*;

/**
 * Модель данных для хранения файлов прикрепляемых к объектам системы
 *
 * @author Valery Orlov
 *         Date: 17.04.2014
 *         Time: 23:15
 */
@Entity
@Table(name = "FILE_CONTAINER",
        indexes = {
                @Index(columnList = FileContainer.OWNER_ID_COLUMN),
                @Index(columnList = "NAME")
        })
public class FileContainer extends AuditedObject {

    public static final String OWNER_ID_COLUMN = "OWNER_ID";

    // Id объекта к которому привязан файл
    @Column(name = OWNER_ID_COLUMN, length = ID_SIZE)
    private String ownerId;

    // Описание файла
    @Column
    private String description;

    // Имя файла
    @Column
    private String name;

    // Тип
    @Column
    private String mimeType;

    // Размер
    @Column
    private long fileSize;

    // Данные файла
    @Lob
    @Basic(fetch= FetchType.LAZY)
    private byte[] fileData;


    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String parentId) {
        this.ownerId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
