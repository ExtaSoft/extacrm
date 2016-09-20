package ru.extas.model.sale;

import ru.extas.model.motor.MotorInstance;

import javax.persistence.*;

/**
 * Экземпляр техники в продаже
 *
 * Created by valery on 16.09.16.
 */
@Entity
@Table(name = "SALE_MOTOR")
public class SaleMotor extends MotorInstance {

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private Sale sale;

    public SaleMotor(final Sale sale, final MotorInstance instance) {
        super(instance);
        this.sale = sale;
    }

    public SaleMotor() {
    }

    public SaleMotor(final Sale sale) {
        this.sale = sale;
    }


    public Sale getSale() {
        return sale;
    }

    public void setSale(final Sale sale) {
        this.sale = sale;
    }
}
