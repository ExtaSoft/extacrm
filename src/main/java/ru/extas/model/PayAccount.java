package ru.extas.model;


import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Платежные реквизиты контрагента
 *
 * @author Valery Orlov
 *         Date: 26.08.13
 *         Time: 13:05
 */
@Entity
@Table(name = "PAY_ACCOUNT")
public class PayAccount extends AbstractExtaObject {

    private static final long serialVersionUID = -7891940552175752858L;

    // Наименование банка
    private String bankName;
    // БИК
    private String bankCode;
    // Корреспондентский счет
    private String loroAccount;
    // Номер расчетного счета
    private String settlementAccount;
    // Контакт которому относится счет
    @ManyToOne
    private Company contact;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(final String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(final String bankCode) {
        this.bankCode = bankCode;
    }

    public String getLoroAccount() {
        return loroAccount;
    }

    public void setLoroAccount(final String loroAccount) {
        this.loroAccount = loroAccount;
    }

    public String getSettlementAccount() {
        return settlementAccount;
    }

    public void setSettlementAccount(final String settlementAccount) {
        this.settlementAccount = settlementAccount;
    }

    public Company getContact() {
        return contact;
    }

    public void setContact(final Company contact) {
        this.contact = contact;
    }
}
