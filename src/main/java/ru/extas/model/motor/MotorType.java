package ru.extas.model.motor;

import ru.extas.model.common.AuditedObject;

import javax.persistence.*;
import javax.validation.constraints.Size;
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

    public static final int NAME_LENGTH = 50;

    @Column(nullable = false, length = NAME_LENGTH)
    @Size(max = NAME_LENGTH)
    private String name;

    @ManyToMany(mappedBy = "brandTypes", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH}, targetEntity = MotorBrand.class)
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
    public void setName(final String name) {
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
    public void setBrands(final Set<MotorBrand> brands) {
        this.brands = brands;
    }
}
