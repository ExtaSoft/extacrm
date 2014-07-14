package ru.extas.model.motor;

import ru.extas.model.common.AuditedObject;

import javax.persistence.*;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Тип техники
 *
 * @author Valery Orlov
 *         Date: 27.05.2014
 *         Time: 9:36
 * @version $Id: $Id
 * @since 0.5.0
 */
@Entity
@Table(name = "MOTOR_TYPE")
public class MotorType  extends AuditedObject {

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "brandTypes", cascade = CascadeType.REFRESH, targetEntity = MotorBrand.class)
    @OrderBy("name ASC")
    private Set<MotorBrand> brands = newHashSet();

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
     * <p>Getter for the field <code>brands</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<MotorBrand> getBrands() {
        return brands;
    }

    /**
     * <p>Setter for the field <code>brands</code>.</p>
     *
     * @param brands a {@link java.util.Set} object.
     */
    public void setBrands(Set<MotorBrand> brands) {
        this.brands = brands;
    }
}
