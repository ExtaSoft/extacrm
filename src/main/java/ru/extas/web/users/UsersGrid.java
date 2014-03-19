/**
 *
 */
package ru.extas.web.users;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.UserProfile;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Таблица пользователей
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
public class UsersGrid extends ExtaGrid {

	private static final long serialVersionUID = -4385482673967616119L;
	private final static Logger logger = LoggerFactory.getLogger(UsersGrid.class);

	/**
	 * <p>Constructor for UsersGrid.</p>
	 */
	public UsersGrid() {
		super();

	}

	/** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new UsersDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDataContainer<UserProfile> container = new ExtaDataContainer<>(UserProfile.class);
		container.addNestedContainerProperty("contact.name");
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new UIAction("Новый", "Ввод нового пользователя в систему", "icon-user-add") {
			@Override
			public void fire(Object itemId) {
				logger.debug("New User...");
				final BeanItem<UserProfile> newObj = new BeanItem<>(new UserProfile());

				final UserEditForm editWin = new UserEditForm("Ввод нового пользователя в систему", newObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							refreshContainer();
						}
					}
				});
				editWin.showModal();
			}
		});


		actions.add(new DefaultAction("Изменить", "Редактирование данных пользователя", "icon-user-1") {
			@Override
			public void fire(final Object itemId) {
				logger.debug("Edit User...");
				final BeanItem<UserProfile> curObj = new GridItem<>(table.getItem(itemId));

				final UserEditForm editWin = new UserEditForm("Редактирование данных пользователя", curObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							refreshContainerItem(itemId);
						}
					}
				});
				editWin.showModal();
			}
		});
		return actions;
	}

}
