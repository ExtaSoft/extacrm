/**
 *
 */
package ru.extas.web.sale;

import com.vaadin.ui.Component;
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
        ret.add(new AbstractTabInfo("Продажи") {
            @Override
            public Component createComponent() {
                return new SalesGrid();
            }
        });
        return ret;
    }
}
