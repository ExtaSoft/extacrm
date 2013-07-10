/**
 * 
 */
package ru.extas.web.insurance;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.extas.server.ServiceLocator.lookup;

import java.util.Collection;

import ru.extas.model.Policy;
import ru.extas.server.PolicyRegistry;
import ru.extas.web.commons.GridDataDecl;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
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
public class BSOGrid extends CustomComponent {

	private static final long serialVersionUID = 4876073256421755574L;

	// private final Logger logger =
	// LoggerFactory.getLogger(InsuranceGrid.class);

	private final Table table;

	public BSOGrid() {

		CssLayout panel = new CssLayout();
		panel.addStyleName("layout-panel");
		panel.setSizeFull();

		// Запрос данных
		final PolicyRegistry policyRepository = lookup(PolicyRegistry.class);
		final Collection<Policy> policies = policyRepository.loadAll();
		final BeanItemContainer<Policy> beans = new BeanItemContainer<Policy>(Policy.class);
		beans.addAll(policies);

		HorizontalLayout commandBar = new HorizontalLayout();
		commandBar.addStyleName("configure");
		commandBar.setSpacing(true);

		Button newPolyceBtn = new Button("Новый", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				final Policy newObj = new Policy();

				final PolicyEditForm editWin = new PolicyEditForm("Новый бланк", newObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						if (editWin.isSaved()) {
							beans.addBean(newObj);
							table.setValue(newObj);
							Notification.show("Бланки сохранен", Type.TRAY_NOTIFICATION);
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

			@Override
			public void buttonClick(ClickEvent event) {
				// // Взять текущий полис из грида
				final Policy selObj = checkNotNull((Policy) table.getValue(), "No selected row");

				final PolicyEditForm editWin = new PolicyEditForm("Редактировать бланк", selObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						if (editWin.isSaved()) {
							// TODO: Избавиться от дорогой операции обновления
							table.refreshRowCache();
							Notification.show("Полис сохранен", Type.TRAY_NOTIFICATION);
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
		table.setContainerDataSource(beans);
		table.setSizeFull();

		// Обеспечиваем корректную работу кнопок зависящих от выбранной записи
		table.setImmediate(true);
		table.setValue(table.getItem(table.firstItemId()));
		table.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				boolean enableBtb = event.getProperty().getValue() != null;
				editPolyceBtn.setEnabled(enableBtb);
			}
		});

		GridDataDecl ds = new PolicyDataDecl();
		ds.initTableColumns(table);

		panel.addComponent(table);

		setCompositionRoot(panel);
	}

}
