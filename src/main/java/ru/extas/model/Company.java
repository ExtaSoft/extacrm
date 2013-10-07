package ru.extas.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Данные контакта - физ. лица
 *
 * @author Valery Orlov
 */
@Entity
@DiscriminatorValue("COMPANY")
@Table(name = "COMPANY", indexes = {
        @Index(columnList = "FULL_NAME")
})
public class Company extends Contact {

    private static final long serialVersionUID = -5681940552175752858L;

    // Полное название (Юридическое имя)
    @Column(name = "FULL_NAME", length = NAME_LENGTH)
    @Max(NAME_LENGTH)
    private String fullName;

    // Коды (ИНН, ОГРН...)
    @OneToMany(mappedBy = "contact")
    private List<ContactCode> codes = newArrayList();

    // Платежные реквизиты
    @OneToMany(mappedBy = "contact")
    private List<PayAccount> payAccounts = newArrayList();

    public Company() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    public List<ContactCode> getCodes() {
        return codes;
    }

    public void setCodes(final List<ContactCode> codes) {
        this.codes = codes;
    }

    public List<PayAccount> getPayAccounts() {
        return payAccounts;
    }

    public void setPayAccounts(final List<PayAccount> payAccounts) {
        this.payAccounts = payAccounts;
    }
}