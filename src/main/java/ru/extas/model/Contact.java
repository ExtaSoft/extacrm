/**
 *
 */
package ru.extas.model;

import com.google.appengine.datanucleus.annotations.Unowned;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Контактное лицо контрагента, клиент физик или сотрудник
 *
 * @author Valery Orlov
 */
@PersistenceCapable(detachable = "true")
public class Contact extends AbstractExtaObject {

    private static final long serialVersionUID = -2543373135823969745L;
    // Фактический адрес
    @Persistent(defaultFetchGroup = "true")
    @Embedded
    private AddressInfo actualAddress;// = new AddressInfo();
    // Информация о физ. лице
    @Persistent(defaultFetchGroup = "true")
    @Embedded
    private PersonInfo personInfo;// = new PersonInfo();
    // Информация о юр. лице
    @Persistent(defaultFetchGroup = "true")
    @Embedded
    private CompanyInfo companyInfo;// = new CompanyInfo();
    // Тип контакта (физ. лицо/юр. лицо)
    @Persistent
    private Type type = Type.PERSON;
    // Имя пользователя
    @Persistent
    private String name;
    // Телефон
    @Persistent
    private String cellPhone;
    // Эл. почта
    @Persistent
    private String email;
    // Вышестоящая организация
    @Persistent(defaultFetchGroup = "true")
    @Unowned
    private Contact affiliation;

    public Contact getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(final Contact affiliation) {
        this.affiliation = affiliation;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public AddressInfo getActualAddress() {
        return actualAddress;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
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

    public void setPersonInfo(final PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    public void setCompanyInfo(final CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    /**
     * Тип контакта (физ. лицо/юр. лицо)
     */
    public enum Type {
        PERSON, COMPANY
    }


}
