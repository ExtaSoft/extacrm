package ru.extas.web.contacts;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import ru.extas.model.Product;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.product.ProductDataDecl;
import ru.extas.web.product.ProductSelectWindow;

import java.util.ArrayList;
import java.util.List;

import static ru.extas.web.commons.TableUtils.fullInitTable;

/**
 * Обеспечивает редактирование списка продуктов внутри юридического лица
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 16:40
 */
public class LegalProductsField extends CustomField<List> {

	private Table table;
	private BeanItemContainer<Product> container;

	public LegalProductsField() {
		setBuffered(true);
		setWidth(600, Unit.PIXELS);
	}

	@Override
	protected Component initContent() {
		final VerticalLayout fieldLayout = new VerticalLayout();
		fieldLayout.setSpacing(true);

		if (!isReadOnly()) {
			final HorizontalLayout commandBar = new HorizontalLayout();
			commandBar.addStyleName("configure");
			commandBar.setSpacing(true);

			final Button addProdBtn = new Button("Добавить", new Button.ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final Button.ClickEvent event) {

					final ProductSelectWindow selectWindow = new ProductSelectWindow("Выберите продукт");
					selectWindow.addCloseListener(new Window.CloseListener() {

						@Override
						public void windowClose(final Window.CloseEvent e) {
							if (selectWindow.isSelectPressed()) {
								container.addBean(selectWindow.getSelected());
								setValue(container.getItemIds());
								Notification.show("Продукт добавлен", Notification.Type.TRAY_NOTIFICATION);
							}
						}
					});
					selectWindow.showModal();
				}
			});
			addProdBtn.setDescription("Добавить продукт в список доступных данному юридическому лицу");
			addProdBtn.addStyleName("icon-doc-new");
			commandBar.addComponent(addProdBtn);

			final Button delProdBtn = new Button("Удалить", new Button.ClickListener() {
				@Override
				public void buttonClick(final Button.ClickEvent event) {
					if (table.getValue() != null) {
						container.removeItem(table.getValue());
						setValue(container.getItemIds());
					}
				}
			});
			delProdBtn.setDescription("Удалить продукт из списка доступных данному юридическому лицу");
			delProdBtn.addStyleName("icon-trash");
			commandBar.addComponent(delProdBtn);

			fieldLayout.addComponent(commandBar);
		}

		table = new Table();
		table.setRequired(true);
		table.setSelectable(true);
		table.setColumnCollapsingAllowed(true);
		//table.setHeight(10, Unit.EM);
		//table.setWidth(25, Unit.EM);
		final Property dataSource = getPropertyDataSource();
		final List<Product> list = dataSource != null ? (List<Product>) dataSource.getValue() : new ArrayList<Product>();
		container = new BeanItemContainer<>(Product.class);
		if (list != null) {
			for (final Product item : list) {
				container.addBean(item);
			}
		}
		container.addNestedContainerProperty("vendor.name");
		table.setContainerDataSource(container);

		final GridDataDecl dataDecl = new ProductDataDecl();
		fullInitTable(table, dataDecl);

		fieldLayout.addComponent(table);

		return fieldLayout;
	}

	@Override
	public Class<? extends List> getType() {
		return List.class;
	}
}
