package ru.extas.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Коды контрагентов (ИНН, ОГРН...)
 *
 * @author Valery Orlov
 *         Date: 26.08.13
 *         Time: 12:53
 */
@PersistenceCapable(detachable = "true")
public class ContactCode {

    // Тип кода (ИНН, ОГРН...)
    @Persistent
    private String type;
    // Значение кода
    @Persistent
    private String code;
    // Контакт которому относится код
    @Persistent
    private Contact contact;

    public Contact getContact() {
        return contact;
    }

    public void setContact(final Contact contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }
}
