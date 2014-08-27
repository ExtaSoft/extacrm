/**
 *
 */
package ru.extas.web.lead;

import com.vaadin.ui.Component;
import ru.extas.model.lead.Lead;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.SubdomainView;
import ru.extas.web.commons.component.AbstractSubdomainInfo;
import ru.extas.web.commons.component.SubdomainInfo;

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
public class LeadsView extends SubdomainView {

    private static final long serialVersionUID = -1272779672761523416L;

    /**
     * <p>Constructor for LeadsView.</p>
     */
    public LeadsView() {
        super("Входящие лиды");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected List<SubdomainInfo> getSubdomainInfo() {
        final ArrayList<SubdomainInfo> ret = newArrayList();
        ret.add(new AbstractSubdomainInfo("Новые", ExtaDomain.LEADS_NEW) {

            @Override
            public ExtaGrid createGrid() {
                return new LeadsGrid(Lead.Status.NEW);
            }
        });
        ret.add(new AbstractSubdomainInfo("Квалифицированные", ExtaDomain.LEADS_QUAL) {

            @Override
            public ExtaGrid createGrid() {
                return new LeadsGrid(Lead.Status.QUALIFIED);
            }
        });
        ret.add(new AbstractSubdomainInfo("Закрытые", ExtaDomain.LEADS_CLOSED) {

            @Override
            public ExtaGrid createGrid() {
                return new LeadsGrid(Lead.Status.CLOSED);
            }
        });
        return ret;
    }
}
