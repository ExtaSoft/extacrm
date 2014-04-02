/**
 *
 */
package ru.extas.web.sale;

import com.vaadin.ui.Component;
import ru.extas.security.ExtaDomain;
import ru.extas.web.commons.AbstractTabView;
import ru.extas.web.commons.component.AbstractTabInfo;
import ru.extas.web.commons.component.TabInfo;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Реализует раздел продаж
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class SalesView extends AbstractTabView {

    private static final long serialVersionUID = -1272779672761523416L;

    /**
     * <p>Constructor for SalesView.</p>
     */
    public SalesView() {
        super("Продажи");
    }


    /** {@inheritDoc} */
    @Override
    protected List<TabInfo> getTabComponentsInfo() {
        final ArrayList<TabInfo> ret = newArrayList();
        ret.add(new AbstractTabInfo("Открытые", ExtaDomain.SALES_OPENED) {
            @Override
            public Component createComponent() {
                return new SalesGrid(ExtaDomain.SALES_OPENED);
            }
        });
        ret.add(new AbstractTabInfo("Завершенные", ExtaDomain.SALES_SUCCESSFUL) {
            @Override
            public Component createComponent() {
                return new SalesGrid(ExtaDomain.SALES_SUCCESSFUL);
            }
        });
        ret.add(new AbstractTabInfo("Отмененные", ExtaDomain.SALES_CANCELED) {
            @Override
            public Component createComponent() {
                return new SalesGrid(ExtaDomain.SALES_CANCELED);
            }
        });
        return ret;
    }
}
