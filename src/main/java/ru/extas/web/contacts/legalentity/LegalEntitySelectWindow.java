package ru.extas.web.contacts.legalentity;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.DefaultAction;
import ru.extas.web.commons.ExtaDataContainer;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.window.CloseOnlylWindow;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.web.commons.GridItem.extractBean;

/**
 * Окно с таблицей для выбора юр. лица
 *
 * @author Valery Orlov
 *         Date: 13.02.14
 *         Time: 16:26
 * @version $Id: $Id
 * @since 0.3
 */
public class LegalEntitySelectWindow extends CloseOnlylWindow {

    private SupplierSer<Company> companySupplier;
    private LegalEntity selected;
	private boolean selectPressed;

	/**
	 * <p>Constructor for LegalEntitySelectWindow.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 */
	public LegalEntitySelectWindow(final String caption) {
		super(caption);
        setWidth(800, Unit.PIXELS);
        setHeight(600, Unit.PIXELS);
	}

    @Override
    public void attach() {
        setContent(new SelectGrid());
        super.attach();
    }

    /**
	 * <p>isSelectPressed.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSelectPressed() {
		return selectPressed;
	}

	private class SelectGrid extends LegalEntitiesGrid {
		private SelectGrid() {
			super.setCompanySupplier(companySupplier);
		}

		@Override
		protected List<UIAction> createActions() {
			final List<UIAction> actions = newArrayList();

			actions.add(new DefaultAction("Выбрать", "Выбрать выделенный в списке контакт и закрыть окно", Fontello.CHECK) {
				@Override
				public void fire(final Object itemId) {

					selected = extractBean(table.getItem(itemId));
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
	 * @return a {@link ru.extas.model.contacts.LegalEntity} object.
	 */
	public LegalEntity getSelected() {
		return selected;
	}

    public SupplierSer<Company> getCompanySupplier() {
        return companySupplier;
    }

    public void setCompanySupplier(SupplierSer<Company> companySupplier) {
        this.companySupplier = companySupplier;
    }
}
