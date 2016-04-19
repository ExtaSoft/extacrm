package ru.extas.model.contacts;

import ru.extas.model.common.Address;
import ru.extas.model.common.IdentifiedObject;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Недвифимое имущество физ.лица
 *
 * @author Valery Orlov
 *         Date: 11.09.2014
 *         Time: 3:36
 */
@Entity
@Table(name = "PERSON_REALTY")
public class PersonRealty extends IdentifiedObject {

    // Тип недвижимости (Индивидуальный дом, Квартира, Дача, Земельный участок, Гараж, Другое имущество)
    @Column(length = 50)
    @Size(max = 50)
    private String type;
    // Время владения (лет)
    @Column(name = "OWNING_PERIOD")
    private int owningPeriod;
    // Доля владения %
    @Column(precision = 32, scale = 4)
    private BigDecimal part;
    // Общая площадь
    //  - строение (кв.м.)
    @Column(name = "AREA_OF_HOUSE")
    private int areaOfHouse;
    //  - участка (соток)
    @Column(name = "AREA_OF_LAND")
    private int areaOfLand;
    // Адрес объекта недвижимости
    @Size(max = 255)
    private String adress;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ADDRESS_REALTY")
    private Address reaityAdress;

    // Способ приобретения (покупка, наследство/дар, другое)
    @Column(name = "WAY_2_PURCHASE", length = 50)
    @Size(max = 50)
    private String way2purchase;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private Person owner;

    public PersonRealty() {
    }

    public PersonRealty(final Person owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public int getOwningPeriod() {
        return owningPeriod;
    }

    public void setOwningPeriod(final int owningPeriod) {
        this.owningPeriod = owningPeriod;
    }

    public BigDecimal getPart() {
        return part;
    }

    public void setPart(final BigDecimal part) {
        this.part = part;
    }

    public int getAreaOfHouse() {
        return areaOfHouse;
    }

    public void setAreaOfHouse(final int areaOfHouse) {
        this.areaOfHouse = areaOfHouse;
    }

    public int getAreaOfLand() {
        return areaOfLand;
    }

    public void setAreaOfLand(final int areaOfLand) {
        this.areaOfLand = areaOfLand;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(final String adress) {
        this.adress = adress;
    }

    public String getWay2purchase() {
        return way2purchase;
    }

    public void setWay2purchase(final String way2purchase) {
        this.way2purchase = way2purchase;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(final Person owner) {
        this.owner = owner;
    }

    public Address getReaityAdress() {
        return reaityAdress;
    }

    public void setReaityAdress(final Address reaityAdress) {
        this.reaityAdress = reaityAdress;
    }
}
