package ru.extas.model.motor;

import ru.extas.model.common.IdentifiedObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Объект конкретной техники с ценой
 *
 * Created by valery on 16.09.16.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MotorInstance extends IdentifiedObject {

    // Тип техники
    @Column(name = "TYPE", length = MotorType.NAME_LENGTH)
    @Size(max = MotorType.NAME_LENGTH)
    private String type;

    // Марка техники
    @Column(name = "BRAND", length = MotorBrand.NAME_LENGTH)
    @Size(max = MotorBrand.NAME_LENGTH)
    private String brand;

    // Модель техники
    @Column(name = "MODEL", length = MotorModel.NAME_LENGTH)
    @Size(max = MotorModel.NAME_LENGTH)
    private String model;

    // Стоимость техники
    @Column(name = "PRICE", precision = 32, scale = 4)
    private BigDecimal price;

    public MotorInstance(final MotorInstance instance) {
        this.type = instance.getType();
        this.brand = instance.getBrand();
        this.model = instance.getModel();
        this.price = instance.getPrice();
    }

    public MotorInstance() {
    }

    public String getType() {
        return type;
    }

    public void setType(final String motorType) {
        this.type = motorType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(final String motorBrand) {
        this.brand = motorBrand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(final String motorModel) {
        this.model = motorModel;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal motorPrice) {
        this.price = motorPrice;
    }
}
