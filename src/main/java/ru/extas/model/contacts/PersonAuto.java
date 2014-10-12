package ru.extas.model.contacts;

import org.joda.time.LocalDate;
import ru.extas.model.common.IdentifiedObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Автотранспорт во владении физ. лица
 *
 * @author Valery Orlov
 *         Date: 11.09.2014
 *         Time: 3:37
 */
@Entity
@Table(name = "PERSON_AUTO")
public class PersonAuto extends IdentifiedObject {

    // Марка, модель
    @Column(name = "BRAND_MODEL", length = 50)
    @Size(max = 50)
    private String brandModel;
    // год выпуска
    @Column(name = "YEAR_OF_MANUFACTURE")
    private int yearOfManufacture;
    // Гос. рег. №
    @Column(name = "REG_NUM", length = 12)
    @Size(max = 12)
    private String regNum;
    // Покупная стоимость
    @Column(precision = 32, scale = 4)
    private BigDecimal price;
    // Дата приобретения
    @Column(name = "PURCHASE_DATE")
    private LocalDate purchaseDate;
    // Способ приобретения (покупка, покупка с пробегом, автокредит, покупка по ген. довер.)
    @Column(name = "WAY_2_PURCHASE", length = 50)
    @Size(max = 50)
    private String way2purchase;

    @ManyToOne
    private Person owner;

    public PersonAuto() {
    }

    public PersonAuto(Person owner) {
        this.owner = owner;
    }

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getWay2purchase() {
        return way2purchase;
    }

    public void setWay2purchase(String way2purchase) {
        this.way2purchase = way2purchase;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }
}
