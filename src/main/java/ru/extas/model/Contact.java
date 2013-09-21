/**
 *
 */
package ru.extas.model;


import javax.persistence.*;

/**
 * Контактное лицо контрагента, клиент физик или сотрудник
 *
 * @author Valery Orlov
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE")
@Table(name = "CONTACT")
public abstract class Contact extends AbstractExtaObject {

    private static final long serialVersionUID = -2543373135823969745L;
    // Фактический адрес
    @Embedded
    private AddressInfo actualAddress;
    // Имя пользователя
    private String name;
    // Телефон
    @Column(name = "CELL_PHONE")
    private String cellPhone;
    // Эл. почта
    private String email;
    // Вышестоящая организация
    @OneToOne
    private Company affiliation;

    public Company getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(final Company affiliation) {
        this.affiliation = affiliation;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the cellPhone
     */
    public String getCellPhone() {
        return cellPhone;
    }

    /**
     * @param cellPhone the cellPhone to set
     */
    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public void setActualAddress(final AddressInfo actualAddress) {
        this.actualAddress = actualAddress;
    }

    public AddressInfo getActualAddress() {
        return actualAddress;
    }

}
