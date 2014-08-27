/**
 *
 */
package ru.extas.web.sale;

import com.vaadin.ui.Component;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.SubdomainView;
import ru.extas.web.commons.component.AbstractSubdomainInfo;
import ru.extas.web.commons.component.SubdomainInfo;

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
public class SalesView extends SubdomainView {

    private static final long serialVersionUID = -1272779672761523416L;

    /**
     * <p>Constructor for SalesView.</p>
     */
    public SalesView() {
        super("Продажи");
    }


    /** {@inheritDoc} */
    @Override
    protected List<SubdomainInfo> getSubdomainInfo() {
        final ArrayList<SubdomainInfo> ret = newArrayList();
        ret.add(new AbstractSubdomainInfo("Открытые", ExtaDomain.SALES_OPENED) {
            @Override
            public ExtaGrid createGrid() {
                return new SalesGrid(ExtaDomain.SALES_OPENED);
            }
        });
        ret.add(new AbstractSubdomainInfo("Завершенные", ExtaDomain.SALES_SUCCESSFUL) {
            @Override
            public ExtaGrid createGrid() {
                return new SalesGrid(ExtaDomain.SALES_SUCCESSFUL);
            }
        });
        ret.add(new AbstractSubdomainInfo("Отмененные", ExtaDomain.SALES_CANCELED) {
            @Override
            public ExtaGrid createGrid() {
                return new SalesGrid(ExtaDomain.SALES_CANCELED);
            }
        });
        return ret;
    }
}
