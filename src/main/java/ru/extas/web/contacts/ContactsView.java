/**
 *
 */
package ru.extas.web.contacts;

import com.vaadin.ui.Component;
import ru.extas.web.commons.AbstractTabView;
import ru.extas.web.commons.component.AbstractTabInfo;
import ru.extas.web.commons.component.TabInfo;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Реализует экран контактов
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
public class ContactsView extends AbstractTabView {

	private static final long serialVersionUID = -1272779672761523416L;

	/**
	 * <p>Constructor for ContactsView.</p>
	 */
	public ContactsView() {
		super("Контакты");
	}

	/** {@inheritDoc} */
	@Override
	protected List<TabInfo> getTabComponentsInfo() {
		final ArrayList<TabInfo> ret = newArrayList();
		ret.add(new AbstractTabInfo("Физические лица") {
			private static final long serialVersionUID = 1L;

			@Override
			public Component createComponent() {
				return new PersonsGrid();
			}
		});
		ret.add(new AbstractTabInfo("Компании") {
			private static final long serialVersionUID = 1L;

			@Override
			public Component createComponent() {
				return new CompaniesGrid();
			}
		});
		ret.add(new AbstractTabInfo("Юридические лица") {
			private static final long serialVersionUID = 1L;

			@Override
			public Component createComponent() {
				return new LegalEntitiesGrid(null);
			}
		});
		ret.add(new AbstractTabInfo("Торговые точки") {
			private static final long serialVersionUID = 1L;

			@Override
			public Component createComponent() {
				return new SalePointsGrid(null);
			}
		});
		return ret;
	}

}
