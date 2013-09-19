package ru.extas.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Данные контакта - физ. лица
 *
 * @author Valery Orlov
 */
@Entity
@DiscriminatorValue("COMPANY")
@Table(name = "company")
public class Company extends Contact {

    private static final long serialVersionUID = -5681940552175752858L;

    // Полное название (Юридическое имя)
    private String fullName;
    // Коды (ИНН, ОГРН...)
    @OneToMany(mappedBy = "contact")
    private List<ContactCode> codes;
    // Платежные реквизиты
    @OneToMany(mappedBy = "contact")
    private List<PayAccount> payAccounts;

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