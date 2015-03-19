/**
 *
 */
package ru.extas.web.contacts.company;

import com.vaadin.data.Container;
import ru.extas.model.contacts.Company;
import ru.extas.security.CompanySecurityFilter;
import ru.extas.web.commons.*;

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
    public ExtaEditForm<Company> createEditForm(final Company company, final boolean isInsert) {
        return new CompanyEditForm(company);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected GridDataDecl createDataDecl() {
        return new CompanyDataDecl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Container createContainer() {
        // Запрос данных
        final ExtaJpaContainer<Company> container = new SecuredDataContainer<Company>(new CompanySecurityFilter());
        container.sort(new Object[]{"name"}, new boolean[]{true});
        return container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<UIAction> createActions() {
        final List<UIAction> actions = newArrayList();

        actions.add(new NewObjectAction("Новый", "Ввод новой компании в систему"));
        actions.add(new EditObjectAction("Изменить", "Редактирование контактных данных"));

        return actions;
    }

}
