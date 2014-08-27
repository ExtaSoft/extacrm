package ru.extas.web.contacts;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Person;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.converters.PhoneConverter;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Выбор контакта - физ. лица
 * с возможностью добавления нового
 * <p/>
 * Date: 12.09.13
 * Time: 12:15
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class PersonSelect extends CustomField<Person> {

	private PersonSelectField personSelectField;
	private Label emailField;
	private Label birthdayField;
	private Label phoneField;
	private Button viewBtn;

	/**
	 * <p>Constructor for PersonSelect.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 */
	public PersonSelect(final String caption) {
		this(caption, "");
	}

	/**
	 * <p>Constructor for PersonSelect.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 */
	public PersonSelect(final String caption, final String description) {
		setCaption(caption);
		setDescription(description);
		setBuffered(true);
		addStyleName("bordered-component");
	}

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {

		VerticalLayout container = new VerticalLayout();
		container.setSpacing(true);

		CssLayout nameLay = new CssLayout();
        nameLay.addStyleName("v-component-group");

		personSelectField = new PersonSelectField("Имя", "Введите или выберите имя контакта");
		personSelectField.setInputPrompt("Фамилия Имя Отчество");
		personSelectField.setPropertyDataSource(getPropertyDataSource());
		personSelectField.setNewItemsAllowed(true);
		personSelectField.setNewItemHandler(new AbstractSelect.NewItemHandler() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({"unchecked"})
			@Override
			public void addNewItem(final String newItemCaption) {
                final Person newObj = new Person();
				newObj.setName(newItemCaption);

				final String edFormCaption = "Ввод нового контакта в систему";
				final PersonEditForm editWin = new PersonEditForm(newObj);
				editWin.setModified(true);

                editWin.addCloseFormListener(new ExtaEditForm.CloseFormListener() {
                    @Override
                    public void closeForm(ExtaEditForm.CloseFormEvent event) {
                        if (editWin.isSaved()) {
                            personSelectField.refreshContainer();
                            personSelectField.setValue(newObj.getId());
                        }
                    }
                });
                FormUtils.showModalWin(editWin);
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
							final Person selected = selectWindow.getSelected();
							personSelectField.setConvertedValue(selected);
						}
					}
				});
				selectWindow.showModal();

			}
		});
		searchBtn.setIcon(Fontello.SEARCH_OUTLINE);
                searchBtn.addStyleName("icon-only");
		nameLay.addComponent(searchBtn);

		viewBtn = new Button("Просмотр", new Button.ClickListener() {
			@Override
			public void buttonClick(final Button.ClickEvent event) {
				final Person bean = (Person) personSelectField.getConvertedValue();

				final PersonEditForm editWin = new PersonEditForm(bean);
				editWin.setModified(true);

                editWin.addCloseFormListener(new ExtaEditForm.CloseFormListener() {
                    @Override
                    public void closeForm(ExtaEditForm.CloseFormEvent event) {
						if (editWin.isSaved()) {
							refreshFields(bean);
						}
					}
				});
                FormUtils.showModalWin(editWin);
			}
		});
		viewBtn.setIcon(Fontello.EDIT_3);
        viewBtn.addStyleName("last");
		viewBtn.addStyleName("icon-only");
		nameLay.addComponent(viewBtn);

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
        phoneField.setConverter(lookup(PhoneConverter.class));
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
		setValue(person);

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

	/** {@inheritDoc} */
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
