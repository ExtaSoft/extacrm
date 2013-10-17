/**
 *
 */
package ru.extas.web.lead;

import com.vaadin.ui.Component;
import ru.extas.web.commons.AbstractTabView;
import ru.extas.web.commons.component.AbstractTabInfo;
import ru.extas.web.commons.component.TabInfo;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Реализует экран лидов
 *
 * @author Valery Orlov
 */
public class LeadsView extends AbstractTabView {

    private static final long serialVersionUID = -1272779672761523416L;

    public LeadsView() {
        super("Входящие лиды");
    }


    @Override
    protected List<TabInfo> getTabComponentsInfo() {
        final ArrayList<TabInfo> ret = newArrayList();
        ret.add(new AbstractTabInfo("Новые лиды") {
            private static final long serialVersionUID = 1L;

            @Override
            public Component createComponent() {
                return new LeadsGrid();
            }
        });
        return ret;
    }
}
