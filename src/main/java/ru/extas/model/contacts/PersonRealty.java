package ru.extas.model.contacts;

import ru.extas.model.common.IdentifiedObject;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Недвифимое имущество физ.лица
 *
 * @author Valery Orlov
 *         Date: 11.09.2014
 *         Time: 3:36
 */
@Entity
@Table(name = "PERSON_REALTY")
public class PersonRealty extends IdentifiedObject {
    // Тип недвижимости (Индивидуальный дом, Квартира, Дача, Земельный участок, Гараж, Другое имущество)
    // Время владения
    // Доля владения %
    // Общая площадь
    //  - строение (кв.м.)
    //  - участка (соток)
    // Адрес объекта недвижимости
    // Способ приобретения (покупка, наследство/дар, другое)
}
