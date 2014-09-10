package ru.extas.model.contacts;

import ru.extas.model.common.IdentifiedObject;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Автотранспорт во владении физ. лица
 *
 * @author Valery Orlov
 *         Date: 11.09.2014
 *         Time: 3:37
 */
@Entity
@Table(name = "PERSON_AUTO")
public class PersonAuto extends IdentifiedObject {
    // Марка, модель
    // год выпуска
    // Гос. рег. №
    // Покупная стоимость
    // Дата приобретения
    // Способ приобретения (покупка, покупка с пробегом, автокредит, покупка по ген. довер.)
}
