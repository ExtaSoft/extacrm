package ru.extas.web.commons;

import com.vaadin.data.Container;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.security.SecureAction;

/**
 * @author Valery Orlov
 *         Date: 23.09.2014
 *         Time: 22:52
 */
public class GridUtils {

    public static boolean isPermitEdit(Container container, Object entity) {
        if(container instanceof AbstractSecuredDataContainer && entity instanceof IdentifiedObject) {
            AbstractSecuredDataContainer secContainer = (AbstractSecuredDataContainer) container;
            return secContainer.isItemPermitAction(((IdentifiedObject)entity).getId(), SecureAction.EDIT);
        }
        return true;
    }


    public static boolean isPermitInsert(Container container) {
        if (container instanceof AbstractSecuredDataContainer) {
            AbstractSecuredDataContainer secContainer = (AbstractSecuredDataContainer) container;
            return secContainer.isInsertPermitted();
        }
        return true;
    }
}
