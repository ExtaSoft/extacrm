package ru.extas.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;

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
    @Column(name = "BANK_NAME", length = Contact.NAME_LENGTH)
    @Max(Contact.NAME_LENGTH)
    private String bankName;

    // БИК
    @Column(name = "BANK_CODE", length = 35)
    @Max(35)
    private String bankCode;

    // Корреспондентский счет
    @Column(name = "LORO_ACCOUNT", length = 35)
    @Max(35)
    private String loroAccount;

    // Номер расчетного счета
    @Column(name = "SETTLEMENT_ACCOUNT", length = 35)
    @Max(35)
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
