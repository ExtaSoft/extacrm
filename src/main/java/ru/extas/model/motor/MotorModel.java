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
 * @version $Id: $Id
 * @since 0.5.0
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

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>code</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCode() {
        return code;
    }

    /**
     * <p>Setter for the field <code>code</code>.</p>
     *
     * @param code a {@link java.lang.String} object.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getType() {
        return type;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type a {@link java.lang.String} object.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>brand</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * <p>Setter for the field <code>brand</code>.</p>
     *
     * @param brand a {@link java.lang.String} object.
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }
}
