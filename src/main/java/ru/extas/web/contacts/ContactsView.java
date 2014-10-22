/**
 *
 */
package ru.extas.web.contacts;

import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.SubdomainInfo;
import ru.extas.web.commons.SubdomainInfoImpl;
import ru.extas.web.commons.SubdomainView;
import ru.extas.web.contacts.company.CompaniesGrid;
import ru.extas.web.contacts.employee.EmployeesGrid;
import ru.extas.web.contacts.legalentity.LegalEntitiesGrid;
import ru.extas.web.contacts.person.PersonsGrid;
import ru.extas.web.contacts.salepoint.SalePointsGrid;

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
		ret.add(new SubdomainInfoImpl("Физ. лица", ExtaDomain.PERSON) {
			@Override
			public ExtaGrid createGrid() {
				return new PersonsGrid();
			}
		});
		ret.add(new SubdomainInfoImpl("Компании", ExtaDomain.COMPANY) {
			@Override
			public ExtaGrid createGrid() {
				return new CompaniesGrid();
			}
        });
		ret.add(new SubdomainInfoImpl("Юридические лица", ExtaDomain.LEGAL_ENTITY) {
			@Override
			public ExtaGrid createGrid() {
				return new LegalEntitiesGrid(null);
			}
		});
		ret.add(new SubdomainInfoImpl("Торговые точки", ExtaDomain.SALE_POINT) {
			@Override
			public ExtaGrid createGrid() {
				return new SalePointsGrid(null);
			}
		});
		ret.add(new SubdomainInfoImpl("Сотрудники", ExtaDomain.EMPLOYEE) {
			@Override
			public ExtaGrid createGrid() {
				return new EmployeesGrid();
			}
		});
		return ret;
	}

}
