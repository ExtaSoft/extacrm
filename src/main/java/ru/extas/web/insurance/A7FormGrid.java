/**
 * 
 */
package ru.extas.web.insurance;

import ru.extas.model.A7Form;
import ru.extas.vaadin.addon.jdocontainer.LazyJdoContainer;
import ru.extas.web.commons.GridDataDecl;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;

/**
 * @author Valery Orlov
 * 
 */
public class A7FormGrid extends CustomComponent {

	private static final long serialVersionUID = 6290106109723378415L;
	private final Table table;

	public A7FormGrid() {

		final CssLayout panel = new CssLayout();
		panel.addStyleName("layout-panel");
		panel.setSizeFull();

		// Запрос данных
		final LazyJdoContainer<A7Form> container = new LazyJdoContainer<A7Form>(A7Form.class, 50, null);
		container.addContainerProperty("owner.name", String.class, null, true, false);

		table = new Table();
		table.setContainerDataSource(container);
		table.setSizeFull();

		// Обеспечиваем корректную работу кнопок зависящих от выбранной записи
		table.setImmediate(true);

		final GridDataDecl ds = new A7FormDataDecl();
		ds.initTableColumns(table);

		panel.addComponent(table);

		setCompositionRoot(panel);
	}
}
