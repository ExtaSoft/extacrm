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
import org.apache.shiro.SecurityUtils;
import ru.extas.model.UserRole;
import ru.extas.web.commons.AbstractTabView;
import ru.extas.web.commons.component.AbstractTabInfo;
import ru.extas.web.commons.component.TabInfo;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Раздел страхование
 *
 * @author Valery Orlov
 */
public class InsuranceView extends AbstractTabView {

    private static final long serialVersionUID = -2524035728558575428L;

    public InsuranceView() {
        super("Страхование техники");
    }


    @Override
    protected List<TabInfo> getTabComponentsInfo() {
        final ArrayList<TabInfo> ret = newArrayList();
        ret.add(new AbstractTabInfo("Имущ. страховки") {
            private static final long serialVersionUID = 1L;

            @Override
            public Component createComponent() {
                return new InsuranceGrid();
            }
        });
        if (!SecurityUtils.getSubject().hasRole(UserRole.USER.getName())) {
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
