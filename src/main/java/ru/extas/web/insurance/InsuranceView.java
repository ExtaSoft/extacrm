/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package ru.extas.web.insurance;

import static ru.extas.server.ServiceLocator.lookup;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.Insurance;
import ru.extas.server.InsuranceRepository;
import ru.extas.web.commons.ExtaAbstractView;
import ru.extas.web.commons.GridDataDecl;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * Раздел страхование
 * 
 * @author Valery Orlov
 * 
 */
public class InsuranceView extends ExtaAbstractView {

	private static final long serialVersionUID = -2524035728558575428L;
	private final Logger logger = LoggerFactory.getLogger(InsuranceView.class);

	private Table table;

	public InsuranceView() {
	}

	@Override
	protected Component getContent() {
		logger.info("Creating view content...");
		CssLayout panel = new CssLayout();
		panel.addStyleName("layout-panel");
		panel.setSizeFull();

		// Button configure = new Button();
		// configure.addStyleName("configure");
		// configure.addStyleName("icon-cog");
		// configure.addStyleName("icon-only");
		// configure.addStyleName("borderless");
		// configure.setDescription("Конфигурация");
		// configure.addStyleName("small");
		// configure.addClickListener(new ClickListener() {
		// @Override
		// public void buttonClick(ClickEvent event) {
		// Notification.show("Не реализовано пока");
		// }
		// });
		// panel.addComponent(configure);

		// Запрос данных
		final InsuranceRepository insuranceRepository = lookup(InsuranceRepository.class);
		final Collection<Insurance> insurances = insuranceRepository.loadAll();
		final BeanItemContainer<Insurance> beans = new BeanItemContainer<Insurance>(Insurance.class);
		beans.addAll(insurances);

		HorizontalLayout commandBar = new HorizontalLayout();
		commandBar.addStyleName("configure");
		commandBar.setSpacing(true);

		Button newPolyceBtn = new Button("Создать", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				final Insurance newObj = new Insurance();

				final InsuranceEditForm editWin = new InsuranceEditForm("Новый полис", newObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						if (editWin.isOkPressed()) {
							beans.addBean(newObj);
							table.setValue(newObj);
							Notification.show("Полис сохранен", Type.TRAY_NOTIFICATION);
						}
					}
				});
				editWin.showModal();
			}
		});
		newPolyceBtn.addStyleName("icon-doc-new");
		newPolyceBtn.setDescription("Ввод нового полиса страхования");
		commandBar.addComponent(newPolyceBtn);

		Button editPolyceBtn = new Button("Редактировать", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// Взять текущий полис из грида
				final Insurance selObj = (Insurance) table.getValue();

				final InsuranceEditForm editWin = new InsuranceEditForm("Редактировать полис", selObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						if (editWin.isOkPressed())
							Notification.show("Полис сохранен", Type.TRAY_NOTIFICATION);
					}
				});
				editWin.showModal();
			}
		});
		editPolyceBtn.addStyleName("icon-edit-3");
		editPolyceBtn.setDescription("Редактировать выделенный в списке полис страхования");
		commandBar.addComponent(editPolyceBtn);

		Button printPolyceBtn = new Button("Печать", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// Взять текущий полис из грида
				// final Insurance selObj = (Insurance) table.getValue();

				// TODO: TO Implement: https://code.google.com/p/xdocreport/
				Notification.show("Не реализовано пока");

			}
		});
		printPolyceBtn.addStyleName("icon-print-2");
		printPolyceBtn.setDescription("Создать печатное представление полиса страхования");
		commandBar.addComponent(printPolyceBtn);

		panel.addComponent(commandBar);

		table = new Table("Страховки", beans);

		table.setSizeFull();
		table.setPageLength(0);
		// t.addStyleName("plain");
		// t.addStyleName("borderless");
		// t.setSortEnabled(false);
		// t.setColumnAlignment("Revenue", Align.RIGHT);
		// table.setRowHeaderMode(RowHeaderMode.INDEX);
		table.setSelectable(true);
		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);

		GridDataDecl ds = new InsuranceDataDecl();
		ds.setTableColumnHeaders(table);
		ds.setTableVisibleColumns(table);
		ds.setTableCollapsedColumns(table);

		panel.addComponent(table);
		return panel;
	}

	/**
	 * @return
	 */
	@Override
	protected Component getTitle() {
		final Component title = new Label("Страхование техники");
		title.setSizeUndefined();
		title.addStyleName("h1");
		return title;
	}

}
