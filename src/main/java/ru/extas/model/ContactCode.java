package ru.extas.model;

import javax.persistence.*;
import javax.validation.constraints.Max;

/**
 * Коды контрагентов (ИНН, ОГРН...)
 *
 * @author Valery Orlov
 *         Date: 26.08.13
 *         Time: 12:53
 */
@Entity
@Table(name = "CONTACT_CODE", indexes = {
        @Index(columnList = "TYPE, CODE", unique = true)
})
public class ContactCode extends AbstractExtaObject {

    private static final long serialVersionUID = -7891940552175752834L;


    // Тип кода (ИНН, ОГРН...)
    @Column(length = 20)
    @Max(20)
    private String type;

    // Значение кода
    @Column(length = 35)
    @Max(35)
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
