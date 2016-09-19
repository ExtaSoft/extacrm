package ru.extas.model.lead;

import ru.extas.model.motor.MotorInstance;

import javax.persistence.*;

/**
 * Экземпляр техники в лиде
 *
 * Created by valery on 16.09.16.
 */
@Entity
@Table(name = "LEAD_MOTOR")
public class LeadMotor extends MotorInstance {

    public LeadMotor() {
    }

    public LeadMotor(Lead lead) {
        this.lead = lead;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private Lead lead;

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }
}
