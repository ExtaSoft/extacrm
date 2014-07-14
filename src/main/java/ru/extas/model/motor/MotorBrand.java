package ru.extas.model.motor;

import ru.extas.model.common.AuditedObject;

import javax.persistence.*;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Определяет марку модели
 *
 * @author Valery Orlov
 *         Date: 27.05.2014
 *         Time: 9:36
 * @version $Id: $Id
 * @since 0.5.0
 */
@Entity
@Table(name = "MOTOR_BRAND")
public class MotorBrand  extends AuditedObject {

    @Column(nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.REFRESH, targetEntity = MotorType.class)
    @JoinTable(
            name = "BRAND_TYPE",
            joinColumns = {@JoinColumn(name = "BRAND_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "TYPE_ID", referencedColumnName = "ID")})
    private Set<MotorType> brandTypes = newHashSet();

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
     * <p>Getter for the field <code>brandTypes</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<MotorType> getBrandTypes() {
        return brandTypes;
    }

    /**
     * <p>Setter for the field <code>brandTypes</code>.</p>
     *
     * @param brandTypes a {@link java.util.Set} object.
     */
    public void setBrandTypes(Set<MotorType> brandTypes) {
        this.brandTypes = brandTypes;
    }
}
