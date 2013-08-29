package ru.extas.model;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import java.io.Serializable;
import java.util.List;

/**
 * Данные контакта - физ. лица
 *
 * @author Valery Orlov
 */
@PersistenceCapable
@EmbeddedOnly
public class CompanyInfo implements Serializable {// Дата рождения

    // Полное название (Юридическое имя)
    @Persistent
    private String fullName;
    // Коды (ИНН, ОГРН...)
    @Persistent(mappedBy = "contact")
    @Element(dependent = "true")
    private List<ContactCode> codes;
    // Платежные реквизиты
    @Persistent(mappedBy = "contact")
    @Element(dependent = "true")
    private List<PayAccount> payAccounts;

    public CompanyInfo() {
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