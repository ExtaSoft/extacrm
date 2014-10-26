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

    public static boolean isPermitEdit(final Container container, final Object entity) {
        if(container instanceof AbstractSecuredDataContainer && entity instanceof IdentifiedObject) {
            final AbstractSecuredDataContainer secContainer = (AbstractSecuredDataContainer) container;
            return secContainer.isItemPermitAction(((IdentifiedObject)entity).getId(), SecureAction.EDIT);
        }
        return true;
    }


    public static boolean isPermitInsert(final Container container) {
        if (container instanceof AbstractSecuredDataContainer) {
            final AbstractSecuredDataContainer secContainer = (AbstractSecuredDataContainer) container;
            return secContainer.isInsertPermitted();
        }
        return true;
    }
}
