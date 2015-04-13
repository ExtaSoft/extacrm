package ru.extas.web.commons.container.jpa;

import ru.extas.model.common.IdentifiedObject;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Valery Orlov
 *         Date: 13.04.2015
 *         Time: 16:51
 */
public interface JpaPropertyProvider<TEntityType extends IdentifiedObject> extends Serializable {

    int getNestedIndex(String propertyName);

    <TEntityType extends IdentifiedObject> Class getPropType(String propertyName);

    Object getBeanProp(TEntityType bean, String propertyName);

    void setBeanProp(TEntityType bean, String propertyName, Object newValue);

    boolean isPropReadOnly(String propertyName);

    boolean isNestedProp(Object id);

    Collection<String> getPropertyIds();
}
