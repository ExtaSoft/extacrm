/**
 *
 */
package ru.extas.web.contacts;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import ru.extas.model.contacts.Company;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.*;
import ru.extas.web.commons.AbstractEditForm;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Таблица контактов (компании)
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class CompaniesGrid extends ExtaGrid<Company> {

	private static final long serialVersionUID = 2299363623807745654L;

	/**
	 * <p>Constructor for CompaniesGrid.</p>
	 */
	public CompaniesGrid() {
		super(Company.class);
	}

    @Override
    public AbstractEditForm<Company> createEditForm(Company company) {
        return new CompanyEditForm(company);
    }

    /** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new CompanyDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDataContainer<Company> container = new SecuredDataContainer<>(Company.class, ExtaDomain.COMPANY);
		container.addNestedContainerProperty("actualAddress.region");
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new NewObjectAction("Новый", "Ввод новой компании в систему"));
		actions.add(new EditObjectAction("Изменить", "Редактирование контактных данных"));

		return actions;
	}
}
