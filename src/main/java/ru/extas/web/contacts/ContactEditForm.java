/**
 * 
 */
package ru.extas.web.contacts;

import static ru.extas.server.ServiceLocator.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.Contact;
import ru.extas.model.Contact.Sex;
import ru.extas.server.ContactService;
import ru.extas.server.SupplementService;
import ru.extas.web.commons.AbstractEditForm;
import ru.extas.web.util.ComponentUtil;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * @author Valery Orlov
 * 
 */
public class ContactEditForm extends AbstractEditForm<Contact> {

	private static final long serialVersionUID = -7787385620289376599L;
	private final Logger logger = LoggerFactory.getLogger(ContactEditForm.class);

	// Компоненты редактирования
	@PropertyId("name")
	private TextField nameField;
	@PropertyId("birthday")
	private PopupDateField birthdayField;
	@PropertyId("sex")
	private ComboBox sexField;
	@PropertyId("cellPhone")
	private TextField cellPhoneField;
	@PropertyId("email")
	private TextField emailField;
	@PropertyId("region")
	private ComboBox regionField;
	@PropertyId("city")
	private ComboBox cityField;
	@PropertyId("postIndex")
	private TextField postIndexField;
	@PropertyId("streetBld")
	private TextArea streetBldField;

	/**
	 * @param caption
	 * @param obj
	 */
	public ContactEditForm(final String caption, final BeanItem<Contact> obj) {
		super(caption, obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.web.commons.AbstractEditForm#initObject(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void initObject(final Contact obj) {
		if (obj.getKey() == null) {
			// Инициализируем новый объект
			// TODO: Инициализировать клиента в соответствии с локацией текущего
			// пользователя (регион, город)
			obj.setSex(Contact.Sex.MALE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.web.commons.AbstractEditForm#saveObject(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void saveObject(final Contact obj) {
		logger.debug("Saving contact data...");
		final ContactService contactService = lookup(ContactService.class);
		contactService.persistContact(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.extas.web.commons.AbstractEditForm#checkBeforeSave(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void checkBeforeSave(final Contact obj) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.extas.web.commons.AbstractEditForm#createEditFields(ru.extas.model
	 * .AbstractExtaObject)
	 */
	@Override
	protected FormLayout createEditFields(final Contact obj) {
		final FormLayout form = new FormLayout();

		nameField = new TextField("Имя");
		nameField.setImmediate(true);
		nameField.setColumns(30);
		nameField.setDescription("Введите имя (ФИО) контакта");
		nameField.setInputPrompt("Имя (Отчество) Фамилия");
		nameField.setRequired(true);
		nameField.setRequiredError("Имя контакта не может быть пустым. Пожалуйста введите ФИО контакта.");
		nameField.setNullRepresentation("");
		form.addComponent(nameField);

		sexField = new ComboBox("Пол");
		sexField.setDescription("Укажите пол контакта");
		sexField.setRequired(true);
		sexField.setNullSelectionAllowed(false);
		sexField.setNewItemsAllowed(false);
		ComponentUtil.fillSelectByEnum(sexField, Sex.class);
		form.addComponent(sexField);

		birthdayField = new PopupDateField("Дата рождения");
		birthdayField.setImmediate(true);
		birthdayField.setInputPrompt("31.12.1978");
		birthdayField.setDescription("Введите дату рождения контакта");
		birthdayField.setDateFormat("dd.MM.yyyy");
		birthdayField.setConversionError("{0} не является допустимой датой. Формат даты: ДД.ММ.ГГГГ");
		form.addComponent(birthdayField);

		cellPhoneField = new TextField("Мобильный телефон");
		cellPhoneField.setImmediate(true);
		cellPhoneField.setColumns(20);
		cellPhoneField.setDescription("Введите мобильный телефон в формате +7 XXX XXX XXXX");
		cellPhoneField.setInputPrompt("+7 XXX XXX XXXX");
		cellPhoneField.setNullRepresentation("");
		// TODO: Добавить проверку правильности ввода телефона
		form.addComponent(cellPhoneField);

		emailField = new TextField("E-Mail");
		emailField.setImmediate(true);
		emailField.setColumns(20);
		emailField.setDescription("Введите имя e-mail контакта который будет использоваться для связи");
		emailField.setInputPrompt("e-mail");
		emailField.setNullRepresentation("");
		emailField.addValidator(new EmailValidator("{0} не является допустимым адресом электронной почты."));
		form.addComponent(emailField);

		regionField = new ComboBox("Регион");
		regionField.setDescription("Укажите регион проживания");
		regionField.setInputPrompt("Выберите или начните ввод...");
		regionField.setImmediate(true);
		regionField.setNullSelectionAllowed(false);
		regionField.setNewItemsAllowed(false);
		regionField.setFilteringMode(FilteringMode.CONTAINS);
		regionField.setWidth(18, Unit.EM);
		for (final String item : lookup(SupplementService.class).loadRegions())
			regionField.addItem(item);
		regionField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				final String newRegion = (String)event.getProperty().getValue();
				final String city = lookup(SupplementService.class).findCityByRegion(newRegion);
				if (city != null)
					cityField.setValue(city);
			}
		});
		form.addComponent(regionField);

		cityField = new ComboBox("Город");
		cityField.setDescription("Введите город проживания контакта");
		cityField.setInputPrompt("Город");
		cityField.setImmediate(true);
		cityField.setNewItemsAllowed(true);
		cityField.setFilteringMode(FilteringMode.CONTAINS);
		for (final String item : lookup(SupplementService.class).loadCities())
			cityField.addItem(item);
		cityField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				final String newCity = (String)event.getProperty().getValue();
				final String region = lookup(SupplementService.class).findRegionByCity(newCity);
				if (region != null)
					regionField.setValue(region);
			}
		});
		form.addComponent(cityField);

		postIndexField = new TextField("Почтовый индекс");
		postIndexField.setColumns(8);
		postIndexField.setInputPrompt("Индекс");
		postIndexField.setNullRepresentation("");
		form.addComponent(postIndexField);

		streetBldField = new TextArea("Адрес");
		streetBldField.setColumns(30);
		streetBldField.setRows(5);
		streetBldField.setDescription("Почтовый адрес (улица, дом, корпус, ...)");
		streetBldField.setInputPrompt("Улица, Дом, Корпус и т.д.");
		streetBldField.setNullRepresentation("");
		form.addComponent(streetBldField);

		return form;
	}
}
