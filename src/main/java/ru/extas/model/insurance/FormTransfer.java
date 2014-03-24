/**
 *
 */
package ru.extas.model.insurance;

import org.joda.time.LocalDate;
import ru.extas.model.common.ChangeMarkedObject;
import ru.extas.model.contacts.Person;

import javax.persistence.*;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Данные приема передачи форм строгой отчетности (БСО)
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@Table(name = "FORM_TRANSFER")
public class FormTransfer extends ChangeMarkedObject {

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
    @CollectionTable(name = "FORM_TRANSFER_NUMS",
            joinColumns = {@JoinColumn(name = "FORM_TRANSFER_ID")},
            indexes = {
                    @Index(columnList = "FORM_TRANSFER_ID, FORMNUMS")
            })
    private List<String> formNums = newArrayList();

    /**
     * <p>Getter for the field <code>fromContact</code>.</p>
     *
     * @return the fromContact
     */
    public Person getFromContact() {
        return fromContact;
    }

    /**
     * <p>Setter for the field <code>fromContact</code>.</p>
     *
     * @param fromContact the fromContact to set
     */
    public void setFromContact(final Person fromContact) {
        this.fromContact = fromContact;
    }

    /**
     * <p>Getter for the field <code>toContact</code>.</p>
     *
     * @return the toContact
     */
    public Person getToContact() {
        return toContact;
    }

    /**
     * <p>Setter for the field <code>toContact</code>.</p>
     *
     * @param toContact the toContact to set
     */
    public void setToContact(final Person toContact) {
        this.toContact = toContact;
    }

    /**
     * <p>Getter for the field <code>transferDate</code>.</p>
     *
     * @return the transferDate
     */
    public LocalDate getTransferDate() {
        return transferDate;
    }

    /**
     * <p>Setter for the field <code>transferDate</code>.</p>
     *
     * @param transferDate the transferDate to set
     */
    public void setTransferDate(final LocalDate transferDate) {
        this.transferDate = transferDate;
    }

    /**
     * <p>Getter for the field <code>formNums</code>.</p>
     *
     * @return the formNums
     */
    public List<String> getFormNums() {
        return formNums;
    }

    /**
     * <p>Setter for the field <code>formNums</code>.</p>
     *
     * @param formNums the formNums to set
     */
    public void setFormNums(final List<String> formNums) {
        this.formNums = formNums;
    }

}
