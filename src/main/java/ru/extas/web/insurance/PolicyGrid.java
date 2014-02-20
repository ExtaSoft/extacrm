/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import ru.extas.model.Policy;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Список страховых полисов в рамках БСО
 *
 * @author Valery Orlov
 */
public class PolicyGrid extends ExtaGrid {

	private static final long serialVersionUID = 4876073256421755574L;

	public PolicyGrid() {
	}

	@Override
	protected GridDataDecl createDataDecl() {
		return new PolicyDataDecl();
	}

	@Override
	protected Container createContainer() {
		return new ExtaDataContainer<>(Policy.class);
	}

	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new UIAction("Новый", "Ввод нового бланка", "icon-doc-new") {

			@Override
			public void fire(Object itemId) {
				final BeanItem<Policy> newObj = new BeanItem<>(new Policy());

				final PolicyEditForm editWin = new PolicyEditForm("Новый бланк", newObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							((ExtaDataContainer) container).refresh();
						}
					}
				});
				editWin.showModal();
			}
		});

		actions.add(new DefaultAction("Изменить", "Редактировать выделенный в списке бланк", "icon-edit-3") {
			@Override
			public void fire(final Object itemId) {
				final BeanItem<Policy> curObj = new GridItem<>(table.getItem(itemId));

				final PolicyEditForm editWin = new PolicyEditForm("Редактировать бланк", curObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							((ExtaDataContainer) container).refreshItem(itemId);
						}
					}
				});
				editWin.showModal();
			}
		});

		return actions;
	}
}
