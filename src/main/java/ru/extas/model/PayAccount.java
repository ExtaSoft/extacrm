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
 * @version $Id: $Id
 * @since 0.3
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

    /**
     * <p>Getter for the field <code>bankName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * <p>Setter for the field <code>bankName</code>.</p>
     *
     * @param bankName a {@link java.lang.String} object.
     */
    public void setBankName(final String bankName) {
        this.bankName = bankName;
    }

    /**
     * <p>Getter for the field <code>bankCode</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * <p>Setter for the field <code>bankCode</code>.</p>
     *
     * @param bankCode a {@link java.lang.String} object.
     */
    public void setBankCode(final String bankCode) {
        this.bankCode = bankCode;
    }

    /**
     * <p>Getter for the field <code>loroAccount</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLoroAccount() {
        return loroAccount;
    }

    /**
     * <p>Setter for the field <code>loroAccount</code>.</p>
     *
     * @param loroAccount a {@link java.lang.String} object.
     */
    public void setLoroAccount(final String loroAccount) {
        this.loroAccount = loroAccount;
    }

    /**
     * <p>Getter for the field <code>settlementAccount</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSettlementAccount() {
        return settlementAccount;
    }

    /**
     * <p>Setter for the field <code>settlementAccount</code>.</p>
     *
     * @param settlementAccount a {@link java.lang.String} object.
     */
    public void setSettlementAccount(final String settlementAccount) {
        this.settlementAccount = settlementAccount;
    }

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
}
