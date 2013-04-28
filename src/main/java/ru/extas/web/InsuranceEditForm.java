package ru.extas.web;

import ru.extas.model.Insurance;
import ru.extas.server.InsuranceRepository;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class InsuranceEditForm extends Window {

	private boolean okPressed = false;
	private final HorizontalLayout buttonsPanel = new HorizontalLayout();
	private final Button cancelBtn;

	// Компоненты редактирования
	@PropertyId("regNum")
	private TextField regNumField;
	@PropertyId("chekNum")
	private TextField chekNumField;
	@PropertyId("date")
	private PopupDateField dateField;
	@PropertyId("clientName")
	private TextField clientNameField;
	@PropertyId("clientBirthday")
	private PopupDateField clientBirthdayField;
	@PropertyId("clientPhone")
	private TextField clientPhoneField;
	@PropertyId("clientMale")
	private TextField clientMaleField;
	@PropertyId("motorType")
	private TextField motorTypeField;
	@PropertyId("motorBrand")
	private TextField motorBrandField;
	@PropertyId("motorModel")
	private TextField motorModelField;
	@PropertyId("riskSum")
	private TextField riskSumField;
	@PropertyId("premium")
	private TextField premiumField;
	@PropertyId("paymentDate")
	private PopupDateField paymentDateField;
	@PropertyId("startDate")
	private PopupDateField startDateField;
	@PropertyId("endDate")
	private PopupDateField endDateField;
	@PropertyId("createdBy")
	private TextField createdByField;
	@PropertyId("reseller")
	private TextField resellerField;

	public InsuranceEditForm(String caption, final Insurance obj) {
		super(caption);

		FormLayout form = createEditFields();

		BeanItem<Insurance> item = new BeanItem<Insurance>(obj);

		// Now create a binder
		final FieldGroup binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);

		cancelBtn = new Button("Отмена", new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				binder.discard();
				UI.getCurrent().removeWindow(InsuranceEditForm.this);
			}
		});
		cancelBtn.setStyleName("icon-cancel");

		final Button okBtn = new Button("OK", new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				try {
					okPressed = true;
					binder.commit();
					new InsuranceRepository().create(obj);
				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				UI.getCurrent().removeWindow(InsuranceEditForm.this);
			}
		});

		okBtn.setStyleName("icon-ok");
		this.buttonsPanel.addComponent(okBtn);
		this.buttonsPanel.setComponentAlignment(okBtn, Alignment.MIDDLE_RIGHT);
		this.buttonsPanel.addComponent(cancelBtn);
		this.buttonsPanel.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);
		this.buttonsPanel.setSpacing(true);

		setContent(form);

	}

	private FormLayout createEditFields() {
		// Have some layout
		FormLayout form = new FormLayout();

		EnduserBigdecimalConverter decCnv = new EnduserBigdecimalConverter();
		regNumField = new TextField("Номер полиса");
		form.addComponent(regNumField);
		chekNumField = new TextField("Номер счета");
		form.addComponent(chekNumField);
		dateField = new PopupDateField("Дата договора");
		form.addComponent(dateField);
		clientNameField = new TextField("Клиент - ФИО");
		clientNameField.setWidth(50, Unit.EX);
		form.addComponent(clientNameField);
		clientBirthdayField = new PopupDateField("Клиент - Дата рождения");
		form.addComponent(clientBirthdayField);
		clientPhoneField = new TextField("Клиент - Телефон");
		form.addComponent(clientPhoneField);
		clientMaleField = new TextField("Клиент - Пол");
		form.addComponent(clientMaleField);
		motorTypeField = new TextField("Тип техники");
		form.addComponent(motorTypeField);
		motorBrandField = new TextField("Марка техники");
		form.addComponent(motorBrandField);
		motorModelField = new TextField("Модель техники");
		form.addComponent(motorModelField);
		riskSumField = new TextField("Страховая сумма");
		riskSumField.setConverter(decCnv);
		form.addComponent(riskSumField);
		premiumField = new TextField("Страховая премия");
		premiumField.setConverter(decCnv);
		form.addComponent(premiumField);
		paymentDateField = new PopupDateField("Дата оплаты страховой премии");
		form.addComponent(paymentDateField);
		startDateField = new PopupDateField("Дата начала срока действия договора");
		form.addComponent(startDateField);
		endDateField = new PopupDateField("Дата окончания срока действия договора");
		form.addComponent(endDateField);
		createdByField = new TextField("Сотрудник");
		form.addComponent(createdByField);
		resellerField = new TextField("Салон");
		form.addComponent(resellerField);

		return form;
	}

	@Override
	public void setContent(Component content) {
		if (content != null) {
			final VerticalLayout contentContainer = new VerticalLayout(content, this.buttonsPanel);
			contentContainer.setMargin(true);
			contentContainer.setSpacing(true);
			contentContainer.setComponentAlignment(this.buttonsPanel, Alignment.MIDDLE_RIGHT);
			content = contentContainer;
		}
		super.setContent(content);
	}

	public boolean isOkPressed() {
		return this.okPressed;
	}

	public void showModal() {
		setClosable(true);
		setModal(true);

		UI.getCurrent().addWindow(this);
	}

}
