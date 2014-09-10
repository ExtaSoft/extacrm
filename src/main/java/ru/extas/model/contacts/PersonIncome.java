package ru.extas.model.contacts;

import ru.extas.model.common.IdentifiedObject;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Доход физ. лица
 *
 * @author Valery Orlov
 *         Date: 11.09.2014
 *         Time: 3:40
 */
@Entity
@Table(name = "PERSON_INCOME")
public class PersonIncome extends IdentifiedObject {
    // Тип дохода (Основная зарплата, Зарплата по совместительству, Прочие)
    // Клиент
    // Супруг(а)
}
