/**
 * 
 */
package ru.extas.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/**
 * Базовый абстрактный класс для классов раздела
 * 
 * @author Valery Orlov
 * 
 */
public abstract class ExtaAbstractView extends VerticalLayout implements View {

	private static final long serialVersionUID = -9143359275908526515L;
	private final Logger logger = LoggerFactory.getLogger(ExtaAbstractView.class);

	/**
	 * 
	 */
	public ExtaAbstractView() {
		super();
	}

	/**
	 * @param children
	 */
	public ExtaAbstractView(Component... children) {
		super(children);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		logger.info("Entering view {}...", event.getViewName());
		setSizeFull();
		addStyleName("base-view");

		HorizontalLayout top = new HorizontalLayout();
		top.setWidth("100%");
		top.setSpacing(true);
		top.addStyleName("toolbar");
		addComponent(top);
		final Component title = getTitle();
		top.addComponent(title);
		top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
		top.setExpandRatio(title, 1);

		Button notify = new Button();
		notify.setDescription("Контекстная справка");
		// notify.addStyleName("borderless");
		notify.addStyleName("notifications");
		notify.addStyleName("icon-only");
		notify.addStyleName("icon-help-1");
		notify.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO: Implement context help
				Notification.show("Не реализовано пока");
				// new InsuranceRepositoryJdo().fillRegistry();
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

		row.addComponent(getContent());

	}

	/**
	 * @return
	 */
	protected abstract Component getContent();

	/**
	 * @return
	 */
	protected abstract Component getTitle();

}