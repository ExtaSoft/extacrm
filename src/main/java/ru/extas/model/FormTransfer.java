/**
 *
 */
package ru.extas.model;

import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Данные приема передачи форм строгой отчетности (БСО)
 *
 * @author Valery Orlov
 */
@Entity
@Table(name = "FORM_TRANSFER")
public class FormTransfer extends AbstractExtaObject {

    private static final long serialVersionUID = -3750723587703870668L;

    /**
     * Контакт от которого принимаются бланки
     */
    @OneToOne
    private Person fromContact;

    /**
     * Контакт которому передются бланки
     */
    @OneToOne
    private Person toContact;

    /**
     * Дата прередачи бланков
     */
    private LocalDate transferDate;

    /**
     * Список номеров передаваемых бланков
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "FORM_TRANSFER_NUMS")
    private List<String> formNums = newArrayList();

    /**
     * @return the fromContact
     */
    public Person getFromContact() {
        return fromContact;
    }

    /**
     * @param fromContact the fromContact to set
     */
    public void setFromContact(final Person fromContact) {
        this.fromContact = fromContact;
    }

    /**
     * @return the toContact
     */
    public Person getToContact() {
        return toContact;
    }

    /**
     * @param toContact the toContact to set
     */
    public void setToContact(final Person toContact) {
        this.toContact = toContact;
    }

    /**
     * @return the transferDate
     */
    public LocalDate getTransferDate() {
        return transferDate;
    }

    /**
     * @param transferDate the transferDate to set
     */
    public void setTransferDate(final LocalDate transferDate) {
        this.transferDate = transferDate;
    }

    /**
     * @return the formNums
     */
    public List<String> getFormNums() {
        return formNums;
    }

    /**
     * @param formNums the formNums to set
     */
    public void setFormNums(final List<String> formNums) {
        this.formNums = formNums;
    }

}
