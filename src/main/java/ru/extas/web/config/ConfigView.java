/**
 * 
 */
package ru.extas.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.web.commons.ExtaAbstractView;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * Реализует экран настроек CRM
 * 
 * @author Valery Orlov
 * 
 */
public class ConfigView extends ExtaAbstractView {

	private static final long serialVersionUID = -1272779672761523416L;

	private final Logger logger = LoggerFactory.getLogger(ConfigView.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.web.ExtaAbstractView#getContent()
	 */
	@Override
	protected Component getContent() {
		logger.info("Creating view content...");
		final Component title = new Label("Скоро будет реализовано...");
		title.setSizeUndefined();
		title.addStyleName("h1");
		title.addStyleName("icon-wrench-1");
		HorizontalLayout l = new HorizontalLayout(title);
		l.setSizeFull();
		l.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
		return l;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.web.ExtaAbstractView#getTitle()
	 */
	@Override
	protected Component getTitle() {
		final Component title = new Label("Настройки");
		title.setSizeUndefined();
		title.addStyleName("h1");
		return title;
	}

}