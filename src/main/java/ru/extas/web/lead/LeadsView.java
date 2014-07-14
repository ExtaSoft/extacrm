/**
 *
 */
package ru.extas.web.lead;

import com.vaadin.ui.Component;
import ru.extas.model.lead.Lead;
import ru.extas.model.security.ExtaDomain;
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
 * @version $Id: $Id
 * @since 0.3
 */
public class LeadsView extends AbstractTabView {

    private static final long serialVersionUID = -1272779672761523416L;

    /**
     * <p>Constructor for LeadsView.</p>
     */
    public LeadsView() {
        super("Входящие лиды");
    }


    /** {@inheritDoc} */
    @Override
    protected List<TabInfo> getTabComponentsInfo() {
        final ArrayList<TabInfo> ret = newArrayList();
        ret.add(new AbstractTabInfo("Новые", ExtaDomain.LEADS_NEW) {

            @Override
            public Component createComponent() {
                return new LeadsGrid(Lead.Status.NEW);
            }
        });
        ret.add(new AbstractTabInfo("Квалифицированные", ExtaDomain.LEADS_QUAL) {

            @Override
            public Component createComponent() {
                return new LeadsGrid(Lead.Status.QUALIFIED);
            }
        });
        ret.add(new AbstractTabInfo("Закрытые", ExtaDomain.LEADS_CLOSED) {

            @Override
            public Component createComponent() {
                return new LeadsGrid(Lead.Status.CLOSED);
            }
        });
        return ret;
    }
}
