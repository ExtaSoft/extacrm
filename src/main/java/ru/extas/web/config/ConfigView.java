/**
 *
 */
package ru.extas.web.config;

import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.web.commons.ExtaAbstractView;

/**
 * Реализует экран настроек CRM
 *
 * @author Valery Orlov
 */
public class ConfigView extends ExtaAbstractView {

private static final long serialVersionUID = -1272779672761523416L;

private final static Logger logger = LoggerFactory.getLogger(ConfigView.class);

/*
 * (non-Javadoc)
 *
 * @see ru.extas.web.ExtaAbstractView#getContent()
 */
@Override
protected Component getContent() {
	logger.debug("Creating view content...");
	final Button updateBtn = new Button("Обновить базу", new Button.ClickListener() {
		@Override
		public void buttonClick(final Button.ClickEvent event) {
			updateDataBase();
		}
	});
	final Component title = new Label("Скоро будет реализовано...");
	title.setSizeUndefined();
	title.addStyleName("h1");
	title.addStyleName("icon-wrench-1");
	HorizontalLayout l = new HorizontalLayout(title);
//        l.addComponent(updateBtn);
	l.setSizeFull();
	l.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
	return l;
}

private void updateDataBase() {
//        try {
//            ContactRepository service = lookup(ContactRepository.class);
//            service.updateMissingType();
//            Notification.show("База данных обновлена", Notification.Type.TRAY_NOTIFICATION);
//        } catch (Exception e) {
//            logger.error("База данных не обновлена", e);
//            Notification.show("Ошибки при обновлении базы данных", e.getMessage(), Notification.Type.ERROR_MESSAGE);
//        }
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
