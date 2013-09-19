package ru.extas.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Коды контрагентов (ИНН, ОГРН...)
 *
 * @author Valery Orlov
 *         Date: 26.08.13
 *         Time: 12:53
 */
@Entity
@Table(name = "CONTACT_CODE")
public class ContactCode extends AbstractExtaObject {

    private static final long serialVersionUID = -7891940552175752834L;


    // Тип кода (ИНН, ОГРН...)
    private String type;
    // Значение кода
    private String code;
    // Контакт которому относится код
    @ManyToOne
    private Company contact;

    public Company getContact() {
        return contact;
    }

    public void setContact(final Company contact) {
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
