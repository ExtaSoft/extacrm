package ru.extas.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Адресные данные контакта
 *
 * @author Valery Orlov
 */
@Embeddable
public class AddressInfo implements Serializable {

    private static final long serialVersionUID = -7891940678175752858L;

    // Регион
    private String region;
    // Город
    private String city;
    // Индекс
    private String postIndex;
    // Адрес (улица, дом и т.д.)
    private String streetBld;

    public AddressInfo() {
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getPostIndex() {
        return postIndex;
    }

    public void setPostIndex(final String postIndex) {
        this.postIndex = postIndex;
    }

    public String getStreetBld() {
        return streetBld;
    }

    public void setStreetBld(final String streetBld) {
        this.streetBld = streetBld;
    }
}