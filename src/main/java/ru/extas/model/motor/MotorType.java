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
 */
@Entity
@Table(name = "MOTOR_TYPE")
public class MotorType  extends AuditedObject {

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "brandTypes", cascade = CascadeType.REFRESH, targetEntity = MotorBrand.class)
    @OrderBy("name ASC")
    private Set<MotorBrand> brands = newHashSet();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MotorBrand> getBrands() {
        return brands;
    }

    public void setBrands(Set<MotorBrand> brands) {
        this.brands = brands;
    }
}
