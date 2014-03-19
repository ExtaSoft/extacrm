/**
 * DISCLAIMER
 *
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 *
 * @author jouni@vaadin.com
 *
 */

package ru.extas.web.insurance;

import com.vaadin.ui.Component;
import ru.extas.model.UserRole;
import ru.extas.server.UserManagementService;
import ru.extas.web.commons.AbstractTabView;
import ru.extas.web.commons.component.AbstractTabInfo;
import ru.extas.web.commons.component.TabInfo;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Раздел страхование
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
public class InsuranceView extends AbstractTabView {

    private static final long serialVersionUID = -2524035728558575428L;

    /**
     * <p>Constructor for InsuranceView.</p>
     */
    public InsuranceView() {
        super("Страхование техники");
    }


    /** {@inheritDoc} */
    @Override
    protected List<TabInfo> getTabComponentsInfo() {
        UserManagementService userService = lookup(UserManagementService.class);
        final ArrayList<TabInfo> ret = newArrayList();
        ret.add(new AbstractTabInfo("Имущ. страховки") {
            private static final long serialVersionUID = 1L;

            @Override
            public Component createComponent() {
                return new InsuranceGrid();
            }
        });
        if (!userService.isCurUserHasRole(UserRole.USER)) {
            ret.add(new AbstractTabInfo("Бланки (БСО)") {
                private static final long serialVersionUID = 1L;

                @Override
                public Component createComponent() {
                    return new PolicyGrid();
                }
            });
            ret.add(new AbstractTabInfo("Акты Приема/Передачи") {
                private static final long serialVersionUID = 1L;

                @Override
                public Component createComponent() {
                    return new FormTransferGrid();
                }
            });
        }
        ret.add(new AbstractTabInfo("Квитанции А-7") {
            private static final long serialVersionUID = 1L;

            @Override
            public Component createComponent() {
                return new A7FormGrid();
            }
        });
        return ret;
    }

}
