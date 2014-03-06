/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.Container;
import ru.extas.model.A7Form;
import ru.extas.server.A7FormService;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;
import static ru.extas.web.commons.GridItem.extractBean;

/**
 * @author Valery Orlov
 */
public class A7FormGrid extends ExtaGrid {

	private static final long serialVersionUID = 6290106109723378415L;

	public A7FormGrid() {
	}

	@Override
	protected GridDataDecl createDataDecl() {
		return new A7FormDataDecl();
	}

	@Override
	protected Container createContainer() {
		ExtaDataContainer<A7Form> cnt = new ExtaDataContainer<>(A7Form.class);
		cnt.addNestedContainerProperty("owner.name");
		return cnt;
	}

	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new ItemAction("Утрачен", "Перевести выделенный в списке бланк в \"Утраченные\"", "") {
			@Override
			public void fire(Object itemId) {
				final A7Form.Status status = A7Form.Status.LOST;
				changeStatus(itemId, status);
			}
		});

		actions.add(new ItemAction("Испорчен", "Перевести выделенный в списке бланк в \"Испорченные\"", "") {
			@Override
			public void fire(Object itemId) {
				final A7Form.Status status = A7Form.Status.BROKEN;
				changeStatus(itemId, status);
			}
		});

		return actions;
	}

	private void changeStatus(Object itemId, A7Form.Status status) {
		final A7Form curObj = extractBean(table.getItem(itemId));

		A7FormService formService = lookup(A7FormService.class);
		formService.changeStatus(curObj, status);
		refreshContainerItem(itemId);
	}
}
