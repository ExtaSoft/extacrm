package ru.extas.model.notification;

import ru.extas.model.common.AuditedObject;

import javax.persistence.Entity;
import javax.persistence.Table;

/** Оповещение о событии системы
 *
 * @author Valery Orlov
 *         Date: 26.06.2015
 *         Time: 16:37
 */
@Entity
@Table(name = "NOTICE")
public class Notice extends AuditedObject {
    // Название
    // Тип события:
    //  - Пользователь назначен ответственным за задачу.
    //  - Оповещения о новых лидах.
    //  - Оповещения о комментариях в продаже.
    //  - Оповещения о "протухших"продажах.
    // Тип объекта события (лид, продажа)
    // Объект события
}
