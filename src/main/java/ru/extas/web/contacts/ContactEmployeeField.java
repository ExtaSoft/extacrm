package ru.extas.web.contacts;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import ru.extas.model.Person;
import ru.extas.web.commons.GridDataDecl;

import java.util.ArrayList;
import java.util.List;

import static ru.extas.web.commons.TableUtils.fullInitTable;

/**
 * Реализует ввод/редактирование списка сотрудников для компании и торговой точки
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 13:04
 * @version $Id: $Id
 */
public class ContactEmployeeField extends CustomField<List> {

	private Table table;
	private BeanItemContainer<Person> container;

	/**
	 * <p>Constructor for ContactEmployeeField.</p>
	 */
	public ContactEmployeeField() {
		super.setBuffered(true);
	}

	/** {@inheritDoc} */
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

					final PersonSelectWindow selectWindow = new PersonSelectWindow("Выберите сотрудника");
					selectWindow.addCloseListener(new Window.CloseListener() {

						@Override
						public void windowClose(final Window.CloseEvent e) {
							if (selectWindow.isSelectPressed()) {
								container.addBean(selectWindow.getSelected());
								setValue(container.getItemIds());
								Notification.show("Сотрудник добавлен", Notification.Type.TRAY_NOTIFICATION);
							}
						}
					});
					selectWindow.showModal();
				}
			});
			addProdBtn.setDescription("Добавить физ. лицо в список сотрудников");
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
			delProdBtn.setDescription("Удалить физ. лицо из списка сотрудников");
			delProdBtn.addStyleName("icon-trash");
			commandBar.addComponent(delProdBtn);

			fieldLayout.addComponent(commandBar);
		}

		table = new Table();
		table.setRequired(true);
		table.setSelectable(true);
		table.setColumnCollapsingAllowed(true);
		final Property dataSource = getPropertyDataSource();
		final List<Person> list = dataSource != null ? (List<Person>) dataSource.getValue() : new ArrayList<Person>();
		container = new BeanItemContainer<>(Person.class);
		if (list != null) {
			for (final Person doc : list) {
				container.addBean(doc);
			}
		}
		container.addNestedContainerProperty("actualAddress.region");
		table.setContainerDataSource(container);

		final GridDataDecl dataDecl = new PersonDataDecl();
		fullInitTable(table, dataDecl);

		fieldLayout.addComponent(table);

		return fieldLayout;
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends List> getType() {
		return List.class;
	}

}
