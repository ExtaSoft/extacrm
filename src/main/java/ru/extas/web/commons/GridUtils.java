package ru.extas.web.commons;

import com.vaadin.data.Container;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.security.SecureAction;
import ru.extas.web.commons.container.SecuredDataContainer;

/**
 * @author Valery Orlov
 *         Date: 23.09.2014
 *         Time: 22:52
 */
public class GridUtils {

    public static boolean isPermitEdit(final Container container, final Object entity) {
        if(container instanceof SecuredDataContainer && entity instanceof IdentifiedObject) {
            final SecuredDataContainer secContainer = (SecuredDataContainer) container;
            return secContainer.getSecurityFilter().isItemPermitAction(((IdentifiedObject)entity).getId(), SecureAction.EDIT);
        }
        return true;
    }


    public static boolean isPermitInsert(final Container container) {
        if (container instanceof SecuredDataContainer) {
            final SecuredDataContainer secContainer = (SecuredDataContainer) container;
            return secContainer.getSecurityFilter().isInsertPermitted();
        }
        return true;
    }
}
