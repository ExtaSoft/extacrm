/**
 *
 */
package ru.extas.model;

import org.joda.time.LocalDate;

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

    public enum Sex {
        MALE, FEMALE
    }

    // Имя пользователя
    @Persistent
    private String name;

    // Дата рождения
    @Persistent
    private LocalDate birthday;

    // Пол
    @Persistent
    private Sex sex;

    // Телефон
    @Persistent
    private String cellPhone;

    // Эл. почта
    @Persistent
    private String email;

    // Адрес:
    // Регион
    @Persistent
    private String region;

    // Город
    @Persistent
    private String city;

    // Индекс
    @Persistent
    private String postIndex;

    // Адрес (улица, дом и т.д.)
    @Persistent
    private String streetBld;

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
     * @return the birthday
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * @param birthday the birthday to set
     */
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
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

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the postIndex
     */
    public String getPostIndex() {
        return postIndex;
    }

    /**
     * @param postIndex the postIndex to set
     */
    public void setPostIndex(String postIndex) {
        this.postIndex = postIndex;
    }

    /**
     * @return the streetBld
     */
    public String getStreetBld() {
        return streetBld;
    }

    /**
     * @param streetBld the streetBld to set
     */
    public void setStreetBld(String streetBld) {
        this.streetBld = streetBld;
    }

    /**
     * @return the sex
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(Sex sex) {
        this.sex = sex;
    }

}
