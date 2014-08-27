/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.Container;
import ru.extas.model.insurance.Policy;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Список страховых полисов в рамках БСО
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class PolicyGrid extends ExtaGrid<Policy> {

	private static final long serialVersionUID = 4876073256421755574L;

	/**
	 * <p>Constructor for PolicyGrid.</p>
	 */
	public PolicyGrid() {
        super(Policy.class);
    }

    @Override
    public ExtaEditForm<Policy> createEditForm(Policy policy) {
        return new PolicyEditForm(policy);
    }

    /** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new PolicyDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		return new ExtaDataContainer<>(Policy.class);
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new NewObjectAction("Новый", "Ввод нового бланка"));
		actions.add(new EditObjectAction("Изменить", "Редактировать выделенный в списке бланк"));

		return actions;
	}
}
