/**
 * 
 */
package ru.extas.web.insurance;

import static com.google.common.base.Preconditions.checkNotNull;
import ru.extas.model.Policy;
import ru.extas.vaadin.addon.jdocontainer.LazyJdoContainer;
import ru.extas.web.commons.GridDataDecl;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * Список страховых полисов в рамках БСО
 * 
 * @author Valery Orlov
 * 
 */
public class PolicyGrid extends CustomComponent {

	private static final long serialVersionUID = 4876073256421755574L;

	// private final Logger logger =
	// LoggerFactory.getLogger(InsuranceGrid.class);

	private final Table table;

	public PolicyGrid() {

		final CssLayout panel = new CssLayout();
		panel.addStyleName("layout-panel");
		panel.setSizeFull();

		// Запрос данных
		final LazyJdoContainer<Policy> container = new LazyJdoContainer<Policy>(Policy.class, 50, null);

		final HorizontalLayout commandBar = new HorizontalLayout();
		commandBar.addStyleName("configure");
		commandBar.setSpacing(true);

		final Button newPolyceBtn = new Button("Новый", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(final ClickEvent event) {
				final Object newObjId = table.addItem();
				final BeanItem<Policy> newObj = (BeanItem<Policy>)table.getItem(newObjId);

				final PolicyEditForm editWin = new PolicyEditForm("Новый бланк", newObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							table.select(newObjId);
							Notification.show("Бланк сохранен", Type.TRAY_NOTIFICATION);
						} else {
							table.removeItem(newObjId);
						}
					}
				});
				editWin.showModal();
			}
		});
		newPolyceBtn.addStyleName("icon-doc-new");
		newPolyceBtn.setDescription("Ввод нового бланка");
		commandBar.addComponent(newPolyceBtn);

		final Button editPolyceBtn = new Button("Изменить", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(final ClickEvent event) {
				// // Взять текущий полис из грида
				final Object curObjId = checkNotNull(table.getValue(), "No selected row");
				final BeanItem<Policy> curObj = (BeanItem<Policy>)table.getItem(curObjId);

				final PolicyEditForm editWin = new PolicyEditForm("Редактировать бланк", curObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							Notification.show("Бланк сохранен", Type.TRAY_NOTIFICATION);
						}
					}
				});
				editWin.showModal();
			}
		});
		editPolyceBtn.addStyleName("icon-edit-3");
		editPolyceBtn.setDescription("Редактировать выделенный в списке бланк");
		editPolyceBtn.setEnabled(false);
		commandBar.addComponent(editPolyceBtn);

		panel.addComponent(commandBar);

		table = new Table();
		table.setContainerDataSource(container);
		table.setSizeFull();

		// Обеспечиваем корректную работу кнопок зависящих от выбранной записи
		table.setImmediate(true);
		table.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				final boolean enableBtb = event.getProperty().getValue() != null;
				editPolyceBtn.setEnabled(enableBtb);
			}
		});
// if (table.size() > 0)
// table.select(table.firstItemId());

		final GridDataDecl ds = new PolicyDataDecl();
		ds.initTableColumns(table);

		panel.addComponent(table);

		setCompositionRoot(panel);
	}
}
