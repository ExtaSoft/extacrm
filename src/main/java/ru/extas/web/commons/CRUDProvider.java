package ru.extas.web.commons;

import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.security.ExtaDomain;

/**
 * Интерфейс объединяющий UI редактирования объекта
 *
 * @author Valery Orlov
 *         Date: 26.08.2014
 *         Time: 14:24
 */
public interface CRUDProvider<TEntity extends IdentifiedObject> {

    ExtaDomain getDomain();

    Class<TEntity> getObjClass();

    String getCaption();

    ExtaGrid getGrid();

    AbstractEditForm<TEntity> getEditForm();
}
