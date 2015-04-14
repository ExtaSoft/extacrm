package ru.extas.web.users;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.model.security.UserProfile;
import ru.extas.web.commons.container.ExtaDbContainer;

/**
 * Выбор пользователя по имени
 *
 * @author Valery Orlov
 *         Date: 05.02.14
 *         Time: 22:19
 * @version $Id: $Id
 * @since 0.3
 */
public class UserProfileSelect extends ComboBox {

	/**
	 * <p>Constructor for UserProfileSelect.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 */
	public UserProfileSelect(final String caption) {
		this(caption, "Позволяет выбрать пользователя");
	}

	/**
	 * <p>Constructor for UserProfileSelect.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 */
	public UserProfileSelect(final String caption, final String description) {
		super(caption);
		// Преконфигурация
		setDescription(description);
		setInputPrompt("Выберите...");
		setWidth(25, Unit.EM);
		setImmediate(true);
		setScrollToSelectedItem(true);

		// Инициализация контейнера
		final ExtaDbContainer<UserProfile> container = new ExtaDbContainer<>(UserProfile.class);
		container.addNestedContainerProperty("employee.name");
		container.sort(new Object[]{"employee.name"}, new boolean[]{true});

		// Устанавливаем контент выбора
		setFilteringMode(FilteringMode.CONTAINS);
		setContainerDataSource(container);
		setItemCaptionMode(ItemCaptionMode.PROPERTY);
		setItemCaptionPropertyId("employee.name");
		container.setSingleSelectConverter(this);

		// Функционал добавления нового контакта
		setNullSelectionAllowed(false);
		setNewItemsAllowed(false);
	}
}
