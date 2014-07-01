package ru.extas.model.motor;

import ru.extas.model.common.AuditedObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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

    private String code;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String brand;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
