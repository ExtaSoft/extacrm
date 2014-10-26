package ru.extas.model.contacts;

import ru.extas.model.common.IdentifiedObject;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
    @Size(max = 50)
    private String type;
    // Сумма
    @Column(precision = 32, scale = 4)
    private BigDecimal expense;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private Person owner;

    public PersonExpense() {
    }

    public PersonExpense(final Person owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(final BigDecimal expense) {
        this.expense = expense;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(final Person owner) {
        this.owner = owner;
    }
}
