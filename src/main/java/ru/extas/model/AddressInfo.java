package ru.extas.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Адресные данные контакта
 *
 * @author Valery Orlov
 */
@Embeddable
public class AddressInfo implements Cloneable, Serializable {

    private static final long serialVersionUID = -7891940678175752858L;

    // Регион
    @Column(length = 50)
    @Max(50)
    private String region;

    // Город
    @Column(length = 30)
    @Max(30)
    private String city;

    // Индекс
    @Column(name = "POST_INDEX", length = 6)
    @Max(6)
    @Pattern(regexp = "[0-9]*")
    private String postIndex;

    // Адрес (улица, дом и т.д.)
    @Column(name = "STREET_BLD", length = 255)
    @Max(255)
    private String streetBld;

    public AddressInfo() {
    }

    public AddressInfo(String region, String city, String postIndex, String streetBld) {
        this.region = region;
        this.city = city;
        this.postIndex = postIndex;
        this.streetBld = streetBld;
    }

    @Override
    public AddressInfo clone() {
        AddressInfo newObj = new AddressInfo(region, city, postIndex, streetBld);
        return newObj;
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