package ru.extas.model;

import javax.persistence.*;
import javax.validation.constraints.Max;

/**
 * Коды контрагентов (ИНН, ОГРН...)
 *
 * @author Valery Orlov
 *         Date: 26.08.13
 *         Time: 12:53
 * @version $Id: $Id
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

    /**
     * <p>Getter for the field <code>contact</code>.</p>
     *
     * @return a {@link ru.extas.model.Company} object.
     */
    public Company getContact() {
        return contact;
    }

    /**
     * <p>Setter for the field <code>contact</code>.</p>
     *
     * @param contact a {@link ru.extas.model.Company} object.
     */
    public void setContact(final Company contact) {
        this.contact = contact;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getType() {
        return type;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type a {@link java.lang.String} object.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>code</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCode() {
        return code;
    }

    /**
     * <p>Setter for the field <code>code</code>.</p>
     *
     * @param code a {@link java.lang.String} object.
     */
    public void setCode(final String code) {
        this.code = code;
    }
}
