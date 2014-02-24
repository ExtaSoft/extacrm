package ru.extas.web.contacts;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.Person;

/**
 * Выбор контакта - физ. лица
 * с возможностью добавления нового
 * <p/>
 * Date: 12.09.13
 * Time: 12:15
 *
 * @author Valery Orlov
 */
public class PersonSelect extends CustomField<Person> {

	private PersonSelectField personSelectField;
	private Person defNewObj;
	private Label emailField;
	private Label birthdayField;
	private Label phoneField;
	private Button viewBtn;

	public PersonSelect(final String caption) {
		this(caption, new Person());
	}

	public PersonSelect(final String caption, Person defNewObj) {
		this(caption, "", defNewObj);
	}

	public PersonSelect(final String caption, final String description) {
		this(caption, description, new Person());
	}

	public PersonSelect(final String caption, final String description, Person defNewObj) {
		this.defNewObj = defNewObj;
		setCaption(caption);
		setDescription(description);
		setBuffered(true);
		addStyleName("bordered-component");
	}

	@Override
	protected Component initContent() {

		VerticalLayout container = new VerticalLayout();
		container.setSpacing(true);

		HorizontalLayout nameLay = new HorizontalLayout();

		personSelectField = new PersonSelectField("Имя", "Введите или выберите имя контакта");
		personSelectField.setInputPrompt("Фамилия Имя Отчество");
		personSelectField.setPropertyDataSource(getPropertyDataSource());
		personSelectField.setNewItemsAllowed(true);
		personSelectField.setNewItemHandler(new AbstractSelect.NewItemHandler() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({"unchecked"})
			@Override
			public void addNewItem(final String newItemCaption) {
				final BeanItem<Person> newObj;
				newObj = new BeanItem<>(defNewObj.clone());
				if (defNewObj.getName() == null)
					newObj.getBean().setName(newItemCaption);
				newObj.expandProperty("actualAddress");

				final String edFormCaption = "Ввод нового контакта в систему";
				final PersonEditForm editWin = new PersonEditForm(edFormCaption, newObj);
				editWin.setModified(true);

				editWin.addCloseListener(new Window.CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final Window.CloseEvent e) {
						if (editWin.isSaved()) {
							personSelectField.refreshContainer();
							personSelectField.setValue(newObj.getBean().getId());
						}
					}
				});
				editWin.showModal();
			}
		});
		personSelectField.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(final Property.ValueChangeEvent event) {
				refreshFields((Person) personSelectField.getConvertedValue());
			}
		});
		nameLay.addComponent(personSelectField);

		Button searchBtn = new Button("Поиск", new Button.ClickListener() {
			@Override
			public void buttonClick(final Button.ClickEvent event) {

				final PersonSelectWindow selectWindow = new PersonSelectWindow("Выберите клиента или введите нового");
				selectWindow.addCloseListener(new Window.CloseListener() {

					@Override
					public void windowClose(final Window.CloseEvent e) {
						if (selectWindow.isSelectPressed()) {
							personSelectField.setConvertedValue(selectWindow.getSelected());
						}
					}
				});
				selectWindow.showModal();

			}
		});
		searchBtn.addStyleName("icon-search-outline");
		searchBtn.addStyleName("icon-only");
		nameLay.addComponent(searchBtn);
		nameLay.setComponentAlignment(searchBtn, Alignment.BOTTOM_LEFT);

		viewBtn = new Button("Просмотр", new Button.ClickListener() {
			@Override
			public void buttonClick(final Button.ClickEvent event) {
				final BeanItem<Person> beanItem;
				beanItem = new BeanItem<>((Person) personSelectField.getConvertedValue());
				beanItem.expandProperty("actualAddress");

				final String edFormCaption = "Просмотр/Редактирование физ. лица";
				final PersonEditForm editWin = new PersonEditForm(edFormCaption, beanItem);
				editWin.setModified(true);

				editWin.addCloseListener(new Window.CloseListener() {

					@Override
					public void windowClose(final Window.CloseEvent e) {
						if (editWin.isSaved()) {
							refreshFields(beanItem.getBean());
						}
					}
				});
				editWin.showModal();
			}
		});
		viewBtn.addStyleName("icon-edit-3");
		viewBtn.addStyleName("icon-only");
		nameLay.addComponent(viewBtn);
		nameLay.setComponentAlignment(viewBtn, Alignment.BOTTOM_LEFT);

		container.addComponent(nameLay);

		HorizontalLayout fieldsContainer = new HorizontalLayout();
		fieldsContainer.setSpacing(true);
		//fieldsContainer.addStyleName("bordered-items");
		// Дата рождения
		birthdayField = new Label();
		birthdayField.setCaption("Дата рождения");
		fieldsContainer.addComponent(birthdayField);
		// Телефон
		phoneField = new Label();
		phoneField.setCaption("Телефон");
		fieldsContainer.addComponent(phoneField);
		// Мыло
		emailField = new Label();
		emailField.setCaption("E-Mail");
		fieldsContainer.addComponent(emailField);
		refreshFields((Person) getPropertyDataSource().getValue());

		container.addComponent(fieldsContainer);

		return container;
	}

	private void refreshFields(Person person) {
		if (person == null) {
			viewBtn.setEnabled(false);
			person = new Person();
		} else
			viewBtn.setEnabled(true);

		BeanItem<Person> personItem = new BeanItem<>(person);
		// Дата рождения
		birthdayField.setPropertyDataSource(personItem.getItemProperty("birthday"));
		// Телефон
		phoneField.setPropertyDataSource(personItem.getItemProperty("phone"));
		// Мыло
		emailField.setPropertyDataSource(personItem.getItemProperty("email"));
	}

	@Override
	public Class<? extends Person> getType() {
		return Person.class;
	}

	private class PersonSelectField extends AbstractContactSelect<Person> {

		protected PersonSelectField(final String caption) {
			super(caption, Person.class);
		}

		protected PersonSelectField(final String caption, final String description) {
			super(caption, description, Person.class);
		}
	}
}
