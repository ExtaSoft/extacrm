package ru.extas.model.motor;

import ru.extas.model.common.AuditedObject;

import javax.persistence.*;

/**
 * Определяет модель техники
 *
 * @author Valery Orlov
 *         Date: 27.05.2014
 *         Time: 9:37
 */
@Entity
@Table(name = "MOTOR_MODEL")
public class MotorModel  extends AuditedObject {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    private MotorType type;

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    private MotorBrand brand;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MotorType getType() {
        return type;
    }

    public void setType(MotorType type) {
        this.type = type;
    }

    public MotorBrand getBrand() {
        return brand;
    }

    public void setBrand(MotorBrand brand) {
        this.brand = brand;
    }
}
