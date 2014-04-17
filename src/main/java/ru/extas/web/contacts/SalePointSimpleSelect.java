package ru.extas.web.contacts;

import com.vaadin.data.util.filter.Compare;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;

/**
 * Выбор контакта - юр. лица
 * с возможностью добавления нового
 * <p>
 * Date: 12.09.13
 * Time: 12:15
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class SalePointSimpleSelect extends AbstractContactSelect<SalePoint> {

    /**
     * <p>Constructor for CompanySelect.</p>
     *
     * @param caption a {@link String} object.
     */
    public SalePointSimpleSelect(final String caption) {
        super(caption, SalePoint.class);
    }

    /**
     * <p>Constructor for CompanySelect.</p>
     *
     * @param caption     a {@link String} object.
     * @param description a {@link String} object.
     */
    public SalePointSimpleSelect(final String caption, final String description) {
        super(caption, description, SalePoint.class);
    }

    public void setContainerFilter(Company company, String region) {
        container.removeAllContainerFilters();
        if (company != null)
            container.addContainerFilter(new Compare.Equal("company", company));
        if (region != null)
            container.addContainerFilter(new Compare.Equal("actualAddress.region", region));
        refreshContainer();
    }
}
