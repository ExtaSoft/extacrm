package ru.extas.web.insurance;

import static ru.extas.server.ServiceLocator.lookup;

import org.joda.time.LocalDate;

import ru.extas.model.Insurance;
import ru.extas.model.Policy;
import ru.extas.server.InsuranceRepository;
import ru.extas.server.PolicyRegistry;
import ru.extas.server.SupplementService;
import ru.extas.web.commons.AbstractEditForm;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.LocalDateField;
import ru.extas.web.contacts.ContactSelect;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

/**
 * Форма ввода/редактирования имущественной страховки
 * 
 * @author Valery Orlov
 * 
 */
public class InsuranceEditForm extends AbstractEditForm<Insurance> {

	private static final long serialVersionUID = 9510268415882116L;

	// Компоненты редактирования
	@PropertyId("regNum")
	private PolicySelect regNumField;
	@PropertyId("date")
	private PopupDateField dateField;
	@PropertyId("client")
	private ComboBox clientNameField;
	@PropertyId("motorType")
	private ComboBox motorTypeField;
	@PropertyId("motorBrand")
	private ComboBox motorBrandField;
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

	public InsuranceEditForm(String caption, final Insurance obj) {
		super(caption, obj);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.extas.web.commons.AbstractEditForm#createEditFields(ru.extas.model
	 * .AbstractExtaObject)
	 */
	@Override
	protected FormLayout createEditFields(Insurance obj) {
		FormLayout form = new FormLayout();

		regNumField = new PolicySelect("Номер полиса", "Введите номер полиса страхования. Выбирается из справочника БСО.");
		regNumField.setRequired(true);
		regNumField.setConverter(String.class);
		regNumField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().getType() == Policy.class) {
					Policy policy = (Policy) event.getProperty().getValue();
					if (policy != null) {
						// Зарезервировать полис в БСО
						lookup(PolicyRegistry.class).bookPolicy(policy);
					}

				}
			}
		});
		form.addComponent(regNumField);

		dateField = new LocalDateField("Дата договора", "Введите дату оформления договора страхования");
		dateField.setRequired(true);
		form.addComponent(dateField);

		clientNameField = new ContactSelect("Страхователь");
		clientNameField.setRequired(true);
		form.addComponent(clientNameField);

		motorTypeField = new ComboBox("Тип техники");
		motorTypeField.setDescription("Укажите тип страхуемой техники");
		motorTypeField.setInputPrompt("Выберите или начните ввод...");
		motorTypeField.setImmediate(true);
		motorTypeField.setNullSelectionAllowed(false);
		motorTypeField.setNewItemsAllowed(false);
		motorTypeField.setFilteringMode(FilteringMode.CONTAINS);
		motorTypeField.setWidth(13, Unit.EM);
		for (String item : lookup(SupplementService.class).loadMotorTypes())
			motorTypeField.addItem(item);
		motorTypeField.setRequired(true);
		form.addComponent(motorTypeField);

		motorBrandField = new ComboBox("Марка техники");
		motorBrandField.setDescription("Укажите марку страхуемой техники");
		motorBrandField.setInputPrompt("Выберите или начните ввод...");
		motorBrandField.setImmediate(true);
		motorBrandField.setNullSelectionAllowed(false);
		motorBrandField.setNewItemsAllowed(false);
		motorBrandField.setFilteringMode(FilteringMode.CONTAINS);
		motorBrandField.setWidth(13, Unit.EM);
		for (String item : lookup(SupplementService.class).loadMotorBrands())
			motorBrandField.addItem(item);
		motorBrandField.setRequired(true);
		form.addComponent(motorBrandField);

		motorModelField = new EditField("Модель техники", "Введите модель техники");
		motorModelField.setRequired(true);
		motorModelField.setColumns(13);
		form.addComponent(motorModelField);

		riskSumField = new EditField("Страховая сумма", "Введите сумму возмещения в рублях");
		riskSumField.setRequired(true);
		form.addComponent(riskSumField);

		premiumField = new EditField("Страховая премия", "Введите стоимость страховки в рублях");
		premiumField.setRequired(true);
		form.addComponent(premiumField);

		paymentDateField = new LocalDateField("Дата оплаты", "Введите дату оплаты страховой премии");
		paymentDateField.setRequired(true);
		paymentDateField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				// пересчитать дату начала действия договора
				LocalDate newDate = (LocalDate) paymentDateField.getConvertedValue();
				LocalDate startDate = ((LocalDate) startDateField.getConvertedValue());
				if (newDate != null && startDate != null && newDate.isAfter(startDate))
					startDateField.setConvertedValue(newDate.plusDays(1));
			}
		});
		form.addComponent(paymentDateField);

		startDateField = new LocalDateField("Дата начала договора", "Введите дату начала действия страхового договора");
		startDateField.setRequired(true);
		startDateField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				// пересчитать дату окончания договора
				LocalDate newDate = (LocalDate) startDateField.getConvertedValue();
				if (newDate != null && endDateField.getPropertyDataSource() != null)
					endDateField.setConvertedValue(newDate.plusYears(1).minusDays(1));
			}
		});
		form.addComponent(startDateField);

		endDateField = new LocalDateField("Дата окончания договора", "Введите дату окончания ");
		endDateField.setRequired(true);
		form.addComponent(endDateField);

		return form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.web.commons.AbstractEditForm#initObject(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void initObject(Insurance obj) {
		if (obj.getKey() == null) {
			final LocalDate now = LocalDate.now();
			obj.setDate(now);
			obj.setPaymentDate(now);
			obj.setStartDate(now);
			obj.setEndDate(now.plusYears(1));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.web.commons.AbstractEditForm#saveObject(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void saveObject(Insurance obj) {
		InsuranceRepository contactService = lookup(InsuranceRepository.class);
		contactService.persist(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.extas.web.commons.AbstractEditForm#checkBeforeSave(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void checkBeforeSave(Insurance obj) {
	}

}
