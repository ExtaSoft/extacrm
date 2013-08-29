package ru.extas.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Платежные реквизиты контрагента
 *
 * @author Valery Orlov
 *         Date: 26.08.13
 *         Time: 13:05
 */
@PersistenceCapable(detachable = "true")
public class PayAccount {

    // Наименование банка
    @Persistent
    private String bankName;
    // БИК
    @Persistent
    private String bankCode;
    // Корреспондентский счет
    @Persistent
    private String loroAccount;
    // Номер расчетного счета
    @Persistent
    private String settlementAccount;
    // Контакт которому относится счет
    @Persistent
    private Contact contact;

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

    public Contact getContact() {
        return contact;
    }

    public void setContact(final Contact contact) {
        this.contact = contact;
    }
}
