package ru.extas.model.common;

import javax.persistence.*;

/**
 * Абстрактный комментарий
 *
 * @author Valery Orlov
 *         Date: 25.09.2014
 *         Time: 18:09
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Comment extends AuditedObject {

    public static final String OWNER_ID_COLUMN = "OWNER_ID";

    // Id объекта к которому привязан комментарий
    @Column(name = OWNER_ID_COLUMN, length = ID_SIZE)
    private String ownerId;

    @Lob
    private String text;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(final String ownerId) {
        this.ownerId = ownerId;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }
}
