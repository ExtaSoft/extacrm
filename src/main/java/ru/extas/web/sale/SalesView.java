/**
 *
 */
package ru.extas.web.sale;

import ru.extas.model.lead.Lead;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.SubdomainInfo;
import ru.extas.web.commons.SubdomainInfoImpl;
import ru.extas.web.commons.SubdomainView;
import ru.extas.web.lead.LeadsGrid;

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
        ret.add(new SubdomainInfoImpl("Новые лиды", ExtaDomain.SALES_LEADS) {
            @Override
            public ExtaGrid createGrid() {
                return new LeadsGrid(Lead.Status.NEW, false);
            }
        });
        ret.add(new SubdomainInfoImpl("Открытые продажи", ExtaDomain.SALES_OPENED) {
            @Override
            public ExtaGrid createGrid() {
                return new SalesGrid(ExtaDomain.SALES_OPENED, false);
            }
        });
        ret.add(new SubdomainInfoImpl("Завершенные продажи", ExtaDomain.SALES_SUCCESSFUL) {
            @Override
            public ExtaGrid createGrid() {
                return new SalesGrid(ExtaDomain.SALES_SUCCESSFUL, false);
            }
        });
        ret.add(new SubdomainInfoImpl("Отмененные продажи", ExtaDomain.SALES_CANCELED) {
            @Override
            public ExtaGrid createGrid() {
                return new SalesGrid(ExtaDomain.SALES_CANCELED, false);
            }
        });
        return ret;
    }
}
