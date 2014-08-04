package ru.extas.web.contacts;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.converters.PhoneConverter;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Компонент выбора торговой точки
 *
 * @author Valery Orlov
 *         Date: 21.02.14
 *         Time: 12:53
 * @version $Id: $Id
 * @since 0.3
 */
public class SalePointSelect extends CustomField<SalePoint> {

	private AbstractContactSelect contactSelect;
	private Button viewBtn;
	private Label companyField;
	private Label phoneField;
	private Label adressField;
	private Company company;

	/**
	 * <p>Constructor for SalePointSelect.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public SalePointSelect(final String caption, final String description, final Company company) {
		this.company = company;
		setCaption(caption);
		setDescription(description);
        setRequiredError(String.format("Поле '%s' не может быть пустым", caption));
		setBuffered(true);
		addStyleName("bordered-component");
	}

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {

		VerticalLayout container = new VerticalLayout();
		container.setSpacing(true);

		HorizontalLayout nameLay = new HorizontalLayout();

		contactSelect = new AbstractContactSelect("Имя", "Введите или выберите имя контакта", SalePoint.class);
		contactSelect.setPropertyDataSource(getPropertyDataSource());
		contactSelect.setNewItemsAllowed(true);
		contactSelect.setNewItemHandler(new AbstractSelect.NewItemHandler() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({"unchecked"})
			@Override
			public void addNewItem(final String newItemCaption) {
				final SalePoint defNewObj = new SalePoint();
				if (defNewObj.getName() == null) {
					defNewObj.setName(newItemCaption);
				}
				defNewObj.setCompany(company);
				final BeanItem<SalePoint> newObj = new BeanItem<>(defNewObj);
				newObj.expandProperty("actualAddress");

				final String edFormCaption = "Ввод нового контакта в систему";
				final SalePointEditForm editWin = new SalePointEditForm(edFormCaption, newObj);
				editWin.setModified(true);

				editWin.addCloseListener(new Window.CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final Window.CloseEvent e) {
						if (editWin.isSaved()) {
							contactSelect.refreshContainer();
							contactSelect.setValue(newObj.getBean().getId());
						}
					}
				});
				editWin.showModal();
			}
		});
		contactSelect.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(final Property.ValueChangeEvent event) {
				refreshFields((SalePoint) contactSelect.getConvertedValue());
			}
		});
		nameLay.addComponent(contactSelect);

		Button searchBtn = new Button("Поиск", new Button.ClickListener() {
			@Override
			public void buttonClick(final Button.ClickEvent event) {

				final SalePointSelectWindow selectWindow = new SalePointSelectWindow("Выберите клиента или введите нового", company);
				selectWindow.addCloseListener(new Window.CloseListener() {

					@Override
					public void windowClose(final Window.CloseEvent e) {
						if (selectWindow.isSelectPressed()) {
							contactSelect.setConvertedValue(selectWindow.getSelected());
						}
					}
				});
				selectWindow.showModal();

			}
		});
		searchBtn.setIcon(Fontello.SEARCH_OUTLINE);
		searchBtn.addStyleName("icon-only");
		nameLay.addComponent(searchBtn);
		nameLay.setComponentAlignment(searchBtn, Alignment.BOTTOM_LEFT);

		viewBtn = new Button("Просмотр", new Button.ClickListener() {
			@Override
			public void buttonClick(final Button.ClickEvent event) {
				final BeanItem<SalePoint> beanItem;
				beanItem = new BeanItem<>((SalePoint) contactSelect.getConvertedValue());
				beanItem.expandProperty("actualAddress");

				final String edFormCaption = "Просмотр/Редактирование торговой точки";
				final SalePointEditForm editWin = new SalePointEditForm(edFormCaption, beanItem);
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
		viewBtn.setIcon(Fontello.EDIT_3);
		viewBtn.addStyleName("icon-only");
		nameLay.addComponent(viewBtn);
		nameLay.setComponentAlignment(viewBtn, Alignment.BOTTOM_LEFT);

		container.addComponent(nameLay);

		HorizontalLayout fieldsContainer = new HorizontalLayout();
		fieldsContainer.setSpacing(true);
		//fieldsContainer.addStyleName("bordered-items");
		// Компания
		companyField = new Label();
		companyField.setCaption("Компания");
		fieldsContainer.addComponent(companyField);
		// Телефон
		phoneField = new Label();
		phoneField.setCaption("Телефон");
        phoneField.setConverter(lookup(PhoneConverter.class));
		fieldsContainer.addComponent(phoneField);
		// Адрес
		adressField = new Label();
		adressField.setCaption("Адрес");
		fieldsContainer.addComponent(adressField);
		refreshFields((SalePoint) getPropertyDataSource().getValue());

		container.addComponent(fieldsContainer);

		return container;
	}

	private void refreshFields(SalePoint salePoint) {
		setValue(salePoint);
		if (salePoint == null) {
			viewBtn.setEnabled(false);
			salePoint = new SalePoint();
		} else
			viewBtn.setEnabled(true);

		BeanItem<SalePoint> personItem = new BeanItem<>(salePoint);
		personItem.addNestedProperty("company.name");
		personItem.addNestedProperty("actualAddress.streetBld");
		// Компания
		companyField.setPropertyDataSource(personItem.getItemProperty("company.name"));
		// Телефон
		phoneField.setPropertyDataSource(personItem.getItemProperty("phone"));
		// Адрес
		adressField.setPropertyDataSource(personItem.getItemProperty("actualAddress.streetBld"));
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends SalePoint> getType() {
		return SalePoint.class;
	}
}
