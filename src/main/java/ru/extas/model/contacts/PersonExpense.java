package ru.extas.model.contacts;

import ru.extas.model.common.IdentifiedObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import java.math.BigDecimal;

/**
 * Расход физ. лица
 *
 * @author Valery Orlov
 *         Date: 11.09.2014
 *         Time: 3:41
 */
@Entity
@Table(name = "PERSON_EXPENSE")
public class PersonExpense extends IdentifiedObject {

    // Тип расхода: Текущие расходы(на питание и одежду), Оплата кредитов, Прочие(указать)
    @Column(length = 50)
    @Max(50)
    private String type;
    // Сумма
    @Column(precision = 32, scale = 4)
    private BigDecimal expense;

    @ManyToOne
    private Person owner;

    public PersonExpense() {
    }

    public PersonExpense(Person owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }
}
