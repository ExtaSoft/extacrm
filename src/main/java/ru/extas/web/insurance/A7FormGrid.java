/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.A7Form;
import ru.extas.model.UserRole;
import ru.extas.server.A7FormService;
import ru.extas.server.UserManagementService;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 */
public class A7FormGrid extends ExtaGrid {

    private static final long serialVersionUID = 6290106109723378415L;

    public A7FormGrid() {
    }

    @Override
    protected GridDataDecl createDataDecl() {
        return new A7FormDataDecl();
    }

    @Override
    protected Container createContainer() {
        JPAContainer<A7Form> cnt = new ExtaDataContainer<>(A7Form.class);
        cnt.addNestedContainerProperty("owner.name");
        UserManagementService userService = lookup(UserManagementService.class);
        // пользователю доступны только собственные записи
        if (userService.isCurUserHasRole(UserRole.USER)) {
            cnt.addContainerFilter(new Compare.Equal("owner", userService.getCurrentUserContact()));
        }
        return cnt;
    }

    @Override
    protected List<UIAction> createActions() {
        List<UIAction> actions = newArrayList();

        actions.add(new ItemAction("Утрачен", "Перевести выделенный в списке бланк в \"Утраченные\"", "") {
            @Override
            public void fire(Object itemId) {
                final A7Form.Status status = A7Form.Status.LOST;
                changeStatus(itemId, status);
            }
        });

        actions.add(new ItemAction("Испорчен", "Перевести выделенный в списке бланк в \"Испорченные\"", "") {
            @Override
            public void fire(Object itemId) {
                final A7Form.Status status = A7Form.Status.BROKEN;
                changeStatus(itemId, status);
            }
        });

        return actions;
    }

    private void changeStatus(Object itemId, A7Form.Status status) {
        final A7Form curObj = ((EntityItem<A7Form>) table.getItem(itemId)).getEntity();

        A7FormService formService = lookup(A7FormService.class);
        formService.changeStatus(curObj, status);
        ((JPAContainer) container).refreshItem(itemId);
    }
}
