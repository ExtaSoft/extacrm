package ru.extas.web.contacts.salepoint;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import ru.extas.server.references.CategoryService;

/**
 * @author Valery Orlov
 *         Date: 27.03.2015
 *         Time: 14:22
 */
public class DealerSalePointField extends SalePointField {
    public DealerSalePointField(String caption, String description) {
        super(caption, description);
        initFilter();
    }

    public DealerSalePointField(String caption, String description, boolean secured) {
        super(caption, description, secured);
        initFilter();
    }

    private void initFilter() {
        final Container.Filter filter = new Compare.Equal("company.categories", CategoryService.COMPANY_CAT_DEALER);
        setFilter(filter);
    }
}
