package ru.extas.model.contacts;

import ru.extas.model.common.IdentifiedObject;

import javax.persistence.Entity;
import javax.persistence.Table;

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
    // Сумма
}
