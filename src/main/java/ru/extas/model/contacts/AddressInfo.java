package ru.extas.model.contacts;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Адресные данные контакта
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Embeddable
public class AddressInfo implements Serializable {

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

    /**
     * <p>Constructor for AddressInfo.</p>
     */
    public AddressInfo() {
    }

    /**
     * <p>Constructor for AddressInfo.</p>
     *
     * @param region a {@link java.lang.String} object.
     * @param city a {@link java.lang.String} object.
     * @param postIndex a {@link java.lang.String} object.
     * @param streetBld a {@link java.lang.String} object.
     */
    public AddressInfo(String region, String city, String postIndex, String streetBld) {
        this.region = region;
        this.city = city;
        this.postIndex = postIndex;
        this.streetBld = streetBld;
    }

    /**
     * <p>Getter for the field <code>region</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getRegion() {
        return region;
    }

    /**
     * <p>Setter for the field <code>region</code>.</p>
     *
     * @param region a {@link java.lang.String} object.
     */
    public void setRegion(final String region) {
        this.region = region;
    }

    /**
     * <p>Getter for the field <code>city</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCity() {
        return city;
    }

    /**
     * <p>Setter for the field <code>city</code>.</p>
     *
     * @param city a {@link java.lang.String} object.
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * <p>Getter for the field <code>postIndex</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPostIndex() {
        return postIndex;
    }

    /**
     * <p>Setter for the field <code>postIndex</code>.</p>
     *
     * @param postIndex a {@link java.lang.String} object.
     */
    public void setPostIndex(final String postIndex) {
        this.postIndex = postIndex;
    }

    /**
     * <p>Getter for the field <code>streetBld</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getStreetBld() {
        return streetBld;
    }

    /**
     * <p>Setter for the field <code>streetBld</code>.</p>
     *
     * @param streetBld a {@link java.lang.String} object.
     */
    public void setStreetBld(final String streetBld) {
        this.streetBld = streetBld;
    }
}
