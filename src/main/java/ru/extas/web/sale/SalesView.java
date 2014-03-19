/**
 *
 */
package ru.extas.web.sale;

import com.vaadin.ui.Component;
import ru.extas.model.Sale;
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
 */
public class SalesView extends AbstractTabView {

    private static final long serialVersionUID = -1272779672761523416L;

    public SalesView() {
        super("Продажи");
    }


    @Override
    protected List<TabInfo> getTabComponentsInfo() {
        final ArrayList<TabInfo> ret = newArrayList();
        ret.add(new AbstractTabInfo("Открытые", ExtaDomain.SALES_OPENED) {
            @Override
            public Component createComponent() {
                return new SalesGrid(Sale.Status.NEW);
            }
        });
        ret.add(new AbstractTabInfo("Завершенные", ExtaDomain.SALES_SUCCESSFUL) {
            @Override
            public Component createComponent() {
                return new SalesGrid(Sale.Status.FINISHED);
            }
        });
        ret.add(new AbstractTabInfo("Отмененные", ExtaDomain.SALES_CANCELED) {
            @Override
            public Component createComponent() {
                return new SalesGrid(Sale.Status.CANCELED);
            }
        });
        return ret;
    }
}
