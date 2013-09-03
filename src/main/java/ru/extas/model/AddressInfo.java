package ru.extas.model;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import java.io.Serializable;

/**
 * Адресные данные контакта
 *
 * @author Valery Orlov
 */
@EmbeddedOnly
@PersistenceCapable(detachable = "true")
public class AddressInfo implements Serializable {

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