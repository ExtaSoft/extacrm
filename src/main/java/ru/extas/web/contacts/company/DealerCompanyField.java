package ru.extas.web.contacts.company;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.contacts.Company_;
import ru.extas.server.references.CategoryService;

/**
 * @author Valery Orlov
 *         Date: 27.03.2015
 *         Time: 14:31
 */
public class DealerCompanyField extends CompanyField {

    public DealerCompanyField(final String caption) {
        super(caption);
        initFilter();
    }

    public DealerCompanyField(final String caption, final String description) {
        super(caption, description);
        initFilter();
    }

    public DealerCompanyField(final String caption, final boolean secured) {
        super(caption, secured);
        initFilter();
    }

    public DealerCompanyField(final String caption, final String description, final boolean secured) {
        super(caption, description, secured);
        initFilter();
    }

    private void initFilter() {
        final Container.Filter filter =
                new Compare.Equal(Company_.categories.getName(), CategoryService.COMPANY_CAT_DEALER);
        setFilter(filter);
    }
}
