/**
 *
 */
package ru.extas.web.contacts;

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
 * Реализует экран контактов
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class ContactsView extends SubdomainView {

	private static final long serialVersionUID = -1272779672761523416L;

	/**
	 * <p>Constructor for ContactsView.</p>
	 */
	public ContactsView() {
		super("Контакты");
	}

	/** {@inheritDoc} */
	@Override
	protected List<SubdomainInfo> getSubdomainInfo() {
		final ArrayList<SubdomainInfo> ret = newArrayList();
		ret.add(new AbstractSubdomainInfo("Контакты", ExtaDomain.PERSON) {
			private static final long serialVersionUID = 1L;

			@Override
			public ExtaGrid createGrid() {
				return new PersonsGrid();
			}


		});
		ret.add(new AbstractSubdomainInfo("Компании", ExtaDomain.COMPANY) {
			private static final long serialVersionUID = 1L;

			@Override
			public ExtaGrid createGrid() {
				return new CompaniesGrid();
			}

        });
		ret.add(new AbstractSubdomainInfo("Юридические лица", ExtaDomain.LEGAL_ENTITY) {
			private static final long serialVersionUID = 1L;

			@Override
			public ExtaGrid createGrid() {
				return new LegalEntitiesGrid(null);
			}
		});
		ret.add(new AbstractSubdomainInfo("Торговые точки", ExtaDomain.SALE_POINT) {
			private static final long serialVersionUID = 1L;

			@Override
			public ExtaGrid createGrid() {
				return new SalePointsGrid(null);
			}
		});
		return ret;
	}

}
