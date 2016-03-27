package ru.extas.web.contacts.salepoint;

import com.vaadin.data.util.filter.Compare;
import com.vaadin.shared.ui.combobox.FilteringMode;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.contacts.SalePoint_;
import ru.extas.web.commons.container.ExtaDbContainer;

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
public class SalePointSimpleSelect extends com.vaadin.ui.ComboBox {

    private static final long serialVersionUID = -8005905898383483037L;
    protected final ExtaDbContainer<SalePoint> container;

    /**
     * <p>Constructor for CompanySelect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public SalePointSimpleSelect(final String caption) {
        this(caption, "Выберите существующий контакт или введите новый");
    }

    /**
     * <p>Constructor for CompanySelect.</p>
     *
     * @param caption     a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     */
    public SalePointSimpleSelect(final String caption, final String description) {
        super(caption);

        // Преконфигурация
        setDescription(description);
        setInputPrompt("контакт...");
        setWidth(25, Unit.EM);
        setImmediate(true);
        setScrollToSelectedItem(true);

        // Инициализация контейнера
        container = new ExtaDbContainer<>(SalePoint.class);
        container.sort(new Object[]{SalePoint_.name.getName()}, new boolean[]{true});

        // Устанавливаем контент выбора
        setFilteringMode(FilteringMode.CONTAINS);
        setContainerDataSource(container);
        setItemCaptionMode(ItemCaptionMode.PROPERTY);
        setItemCaptionPropertyId("name");
        container.setSingleSelectConverter(this);

        // Функционал добавления нового контакта
        setNullSelectionAllowed(false);
        setNewItemsAllowed(false);
    }

    /**
     * <p>setContainerFilter.</p>
     *
     * @param company a {@link ru.extas.model.contacts.Company} object.
     * @param region a {@link java.lang.String} object.
     */
    public void setContainerFilter(final Company company, final String region) {
        container.removeAllContainerFilters();
        if (company != null)
            container.addContainerFilter(new Compare.Equal("company", company));
        if (region != null)
            container.addContainerFilter(new Compare.Equal("posAddress.region", region));//FIXME: С этим надо что-то делать!!!
        refreshContainer();
    }

    /**
     * <p>refreshContainer.</p>
     */
    public void refreshContainer() {
        container.refresh();
    }
}
