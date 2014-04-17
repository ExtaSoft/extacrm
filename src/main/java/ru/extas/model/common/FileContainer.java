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
                @Index(columnList = "PARENT_ID"),
                @Index(columnList = "NAME")
        })
public class FileContainer extends AuditedObject {

    @Column(name = "PARENT_ID", length = ID_SIZE)
    private String parentId;

    @Column
    private String name;

    @Lob
    @Basic(fetch= FetchType.LAZY)
    private byte[] fileData;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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
}
