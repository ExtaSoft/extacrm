package ru.extas.web.contacts;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Person;
import ru.extas.web.commons.converters.PhoneConverter;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Поле для расширенного отображения физ. лица.
 *
 * @author Valery Orlov
 *         Date: 04.02.14
 *         Time: 19:09
 * @version $Id: $Id
 * @since 0.3
 */
public class PersonField extends CustomField<Person> {

	private BeanItem<Person> personItem;

	/**
	 * <p>Constructor for PersonField.</p>
	 */
	public PersonField() {
		this("Клиент");
	}

	/**
	 * <p>Constructor for PersonField.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 */
	public PersonField(String caption) {
		setCaption(caption);
	}

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {
		VerticalLayout container = new VerticalLayout();
		container.setSpacing(true);

		final Person person = (Person) getPropertyDataSource().getValue();
		personItem = new BeanItem<>(person == null ? new Person() : person);

		final Button.ClickListener openLeadFormListener = new Button.ClickListener() {
			@Override
			public void buttonClick(final Button.ClickEvent event) {
				PersonEditForm form = new PersonEditForm("Просмотр/редактирование клиента", personItem);
				form.showModal();
			}
		};
		// Открытие формы ввода/редактирования
		Button openBtn = new Button("Нажмите для просмотра/редактирования клиента...", openLeadFormListener);
		openBtn.addStyleName("link");
		container.addComponent(openBtn);

		HorizontalLayout fieldsContainer = new HorizontalLayout();
		fieldsContainer.setSpacing(true);
		fieldsContainer.addStyleName("bordered-items");
		// Имя
		Label nameField = new Label(personItem.getItemProperty("name"));
		nameField.setCaption("Имя");
		fieldsContainer.addComponent(nameField);
		// Телефон
		Label phoneField = new Label(personItem.getItemProperty("phone"));
		phoneField.setCaption("Телефон");
        phoneField.setConverter(lookup(PhoneConverter.class));
		fieldsContainer.addComponent(phoneField);
		// Мыло
		Label emailField = new Label(personItem.getItemProperty("email"));
		emailField.setCaption("E-Mail");
		fieldsContainer.addComponent(emailField);

		container.addComponent(fieldsContainer);

		return container;
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends Person> getType() {
		return Person.class;
	}
}
