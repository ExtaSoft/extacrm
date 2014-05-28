package ru.extas.model.motor;

import ru.extas.model.common.AuditedObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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

    @Column
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
