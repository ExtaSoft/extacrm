package ru.extas.web.users;

import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.model.UserProfile;
import ru.extas.web.commons.ExtaDataContainer;

/**
 * Выбор пользователя по имени
 *
 * @author Valery Orlov
 *         Date: 05.02.14
 *         Time: 22:19
 */
public class UserProfileSelect extends ComboBox {

	public UserProfileSelect(final String caption) {
		this(caption, "Позволяет выбрать пользователя");
	}

	public UserProfileSelect(final String caption, final String description) {
		super(caption);
		// Преконфигурация
		setDescription(description);
		setInputPrompt("Выберите пользователя или начните ввод...");
		setWidth(25, Unit.EM);
		setImmediate(true);
		setScrollToSelectedItem(true);

		// Инициализация контейнера
		ExtaDataContainer<UserProfile> container = new ExtaDataContainer<>(UserProfile.class);
		container.addNestedContainerProperty("contact.name");

		// Устанавливаем контент выбора
		setFilteringMode(FilteringMode.CONTAINS);
		setContainerDataSource(container);
		setItemCaptionMode(ItemCaptionMode.PROPERTY);
		setItemCaptionPropertyId("contact.name");
		setConverter(new SingleSelectConverter<UserProfile>(this));

		// Функционал добавления нового контакта
		setNullSelectionAllowed(false);
		setNewItemsAllowed(false);
	}
}
