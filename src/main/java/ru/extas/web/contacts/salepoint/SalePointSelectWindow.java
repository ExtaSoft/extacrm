package ru.extas.web.contacts.salepoint;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.DefaultAction;
import ru.extas.web.commons.ExtaDataContainer;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.window.CloseOnlylWindow;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.web.commons.GridItem.extractBean;

/**
 * Окно выбора торговой точки
 *
 * @author Valery Orlov
 *         Date: 21.02.14
 *         Time: 13:40
 * @version $Id: $Id
 * @since 0.3
 */
public class SalePointSelectWindow extends CloseOnlylWindow {
    private Set<SalePoint> selected;
    private boolean selectPressed;

    /**
     * <p>Constructor for SalePointSelectWindow.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param company a {@link ru.extas.model.contacts.Company} object.
     */
    public SalePointSelectWindow(final String caption, final SupplierSer<Company> companySupplier) {
        super(caption);
        setWidth(800, Unit.PIXELS);
        setHeight(600, Unit.PIXELS);
        final SelectGrid grid = new SelectGrid(companySupplier);
        setContent(grid);
    }

    /**
     * <p>isSelectPressed.</p>
     *
     * @return a boolean.
     */
    public boolean isSelectPressed() {
        return selectPressed;
    }

    private class SelectGrid extends SalePointsGrid {
        public SelectGrid(final SupplierSer<Company> companySupplier) {
            setCompanySupplier(companySupplier);
        }

        @Override
        protected Container createContainer() {
            final ExtaDataContainer<SalePoint> container = new ExtaDataContainer<>(SalePoint.class);
            container.addNestedContainerProperty("regAddress.region");
            container.addNestedContainerProperty("regAddress.city");
            container.addNestedContainerProperty("regAddress.streetBld");
            container.addNestedContainerProperty("company.name");
            if (getCompanySupplier() != null)
                container.addContainerFilter(new Compare.Equal("company", getCompanySupplier().get()));
            return container;
        }

        @Override
        protected List<UIAction> createActions() {
            final List<UIAction> actions = newArrayList();

            actions.add(new DefaultAction("Выбрать", "Выбрать выделенный в списке контакт и закрыть окно", Fontello.CHECK) {
                @Override
                public void fire(final Set itemIds) {

                    selected = getEntities(itemIds);
                    selectPressed = true;
                    close();
                }
            });

            actions.addAll(super.createActions());

            return actions;
        }
    }

    /**
     * <p>Getter for the field <code>selected</code>.</p>
     *
     * @return a {@link ru.extas.model.contacts.SalePoint} object.
     */
    public Set<SalePoint> getSelected() {
        return selected;
    }
}
