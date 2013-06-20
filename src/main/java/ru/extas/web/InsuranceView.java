/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package ru.extas.web;

import java.util.Collection;

import ru.extas.model.Insurance;
import ru.extas.server.InsuranceRepository;
import ru.extas.server.InsuranceRepositoryJdo;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * Раздел страхование
 * 
 * @author Valery Orlov
 * 
 */
public class InsuranceView extends VerticalLayout implements View {

	private Table table;

	public InsuranceView() {
	}

	private CssLayout createGrigPanel() {
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

		final InsuranceRepository insuranceRepository = new InsuranceRepositoryJdo();
		final Collection<Insurance> insurances = insuranceRepository.loadAll();
		final BeanItemContainer<Insurance> beans = new BeanItemContainer<Insurance>(Insurance.class);
		beans.addAll(insurances);

		HorizontalLayout commandBar = new HorizontalLayout();
		commandBar.addStyleName("configure");
		commandBar.setSpacing(true);

		Button newPolyceBtn = new Button("Новый полис", new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// Взять текущий полис из грида
				final Insurance newObj = new Insurance();

				final InsuranceEditForm editWin = new InsuranceEditForm("Новый полис", newObj);
				editWin.addCloseListener(new CloseListener() {

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
		commandBar.addComponent(newPolyceBtn);

		Button editPolyceBtn = new Button("Редактировать полис", new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// Взять текущий полис из грида
				final Insurance selObj = (Insurance) table.getValue();

				final InsuranceEditForm editWin = new InsuranceEditForm("Редактировать полис", selObj);
				editWin.addCloseListener(new CloseListener() {

					@Override
					public void windowClose(CloseEvent e) {
						if (editWin.isOkPressed())
							Notification.show("Полис сохранен", Type.TRAY_NOTIFICATION);
					}
				});
				editWin.showModal();
			}
		});
		commandBar.addComponent(editPolyceBtn);

		Button printPolyceBtn = new Button("Печать полиса", new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// Взять текущий полис из грида
				// See: https://code.google.com/p/xdocreport/
				final Insurance selObj = (Insurance) table.getValue();

			}
		});
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

		InsuranceDataSource ds = new InsuranceDataSource();
		ds.setTableColumnHeaders(table);
		ds.setTableVisibleColumns(table);
		ds.setTableCollapsedColumns(table);

		panel.addComponent(table);
		return panel;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();
		addStyleName("insurance-view");

		HorizontalLayout top = new HorizontalLayout();
		top.setWidth("100%");
		top.setSpacing(true);
		top.addStyleName("toolbar");
		addComponent(top);
		final Label title = new Label("Страхование техники");
		title.setSizeUndefined();
		title.addStyleName("h1");
		top.addComponent(title);
		top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
		top.setExpandRatio(title, 1);

		Button notify = new Button();
		notify.setDescription("Создать тестовые данные");
		// notify.addStyleName("borderless");
		notify.addStyleName("notifications");
		notify.addStyleName("icon-only");
		notify.addStyleName("icon-bell");
		notify.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				new InsuranceRepositoryJdo().fillRegistry();
			}
		});
		top.addComponent(notify);
		top.setComponentAlignment(notify, Alignment.MIDDLE_LEFT);

		HorizontalLayout row = new HorizontalLayout();
		row.setMargin(true);
		row.setSizeFull();
		row.setSpacing(true);
		addComponent(row);
		setExpandRatio(row, 2);

		row.addComponent(createGrigPanel());

	}

}
