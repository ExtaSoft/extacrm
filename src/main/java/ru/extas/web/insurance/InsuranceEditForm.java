package ru.extas.web.insurance;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.*;
import org.joda.time.LocalDate;
import ru.extas.model.Insurance;
import ru.extas.model.Policy;
import ru.extas.server.InsuranceCalculator;
import ru.extas.server.InsuranceService;
import ru.extas.server.PolicyService;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.LocalDateField;
import ru.extas.web.commons.converters.StringToPercentConverter;
import ru.extas.web.commons.window.AbstractEditForm;
import ru.extas.web.contacts.PersonSelect;
import ru.extas.web.contacts.SalePointSelect;
import ru.extas.web.reference.MotorBrandSelect;
import ru.extas.web.reference.MotorTypeSelect;
import ru.extas.web.util.ComponentUtil;

import java.math.BigDecimal;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода/редактирования имущественной страховки
 *
 * @author Valery Orlov
 */
public class InsuranceEditForm extends AbstractEditForm<Insurance> {

	private static final long serialVersionUID = 9510268415882116L;
	// Компоненты редактирования
	@PropertyId("regNum")
	private PolicySelect regNumField;
	@PropertyId("a7Num")
	private A7Select a7FormNumField;
	@PropertyId("date")
	private PopupDateField dateField;
	@PropertyId("client")
	private PersonSelect clientNameField;
	@PropertyId("motorType")
	private MotorTypeSelect motorTypeField;
	@PropertyId("motorBrand")
	private MotorBrandSelect motorBrandField;
	@PropertyId("motorModel")
	private TextField motorModelField;
	@PropertyId("riskSum")
	private TextField riskSumField;
	@PropertyId("coverTime")
	private OptionGroup coverTimeField;
	@PropertyId("premium")
	private TextField premiumField;
	@PropertyId("paymentDate")
	private PopupDateField paymentDateField;
	@PropertyId("startDate")
	private PopupDateField startDateField;
	@PropertyId("endDate")
	private PopupDateField endDateField;
	@PropertyId("dealer")
	private SalePointSelect dealerField;
	private Label tarifField;
	private ObjectProperty<BigDecimal> tarifDataSource;

	public InsuranceEditForm(final String caption, final BeanItem<Insurance> obj) {
		super(caption, obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ru.extas.web.commons.window.AbstractEditForm#createEditFields(ru.extas.model
	 * .AbstractExtaObject)
	 */
	@Override
	protected ComponentContainer createEditFields(final Insurance obj) {
		final FormLayout form = new FormLayout();

		regNumField = new PolicySelect("Номер полиса",
				"Введите номер полиса страхования. Выбирается из справочника БСО.", obj.getRegNum());
		regNumField.setRequired(true);
		// regNumField.setConverter(String.class);
		regNumField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(final ValueChangeEvent event) {
				if (event.getProperty().getType() == Policy.class) {
					final Object selItemId = event.getProperty().getValue();
					if (selItemId != null) {
						final BeanItem<Policy> policy = (BeanItem<Policy>) regNumField.getItem(selItemId);
						// Зарезервировать полис в БСО
						lookup(PolicyService.class).bookPolicy(policy.getBean());
					}

				}
			}
		});
		form.addComponent(regNumField);

		a7FormNumField = new A7Select("Номер квитанции А-7",
				"Введите номер квитанции А-7. Выбирается из справочника БСО.", obj.getA7Num());
		// TODO a7FormNumField.setRequired(true);
		form.addComponent(a7FormNumField);

		dateField = new LocalDateField("Дата договора", "Введите дату оформления договора страхования");
		dateField.setRequired(true);
		dateField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				// пересчитать дату оплаты страховки
				final LocalDate newDate = (LocalDate) dateField.getConvertedValue();
				if (newDate != null && paymentDateField.getPropertyDataSource() != null)
					paymentDateField.setConvertedValue(newDate);
			}
		});
		form.addComponent(dateField);

		// FIXME Ограничить выбор контакта только клиентами
		clientNameField = new PersonSelect("Страхователь");
		clientNameField.setRequired(true);
		form.addComponent(clientNameField);

		motorTypeField = new MotorTypeSelect();
		motorTypeField.setRequired(true);
		form.addComponent(motorTypeField);

		motorBrandField = new MotorBrandSelect();
		motorBrandField.setRequired(true);
		motorBrandField.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(final ValueChangeEvent event) {
				calculatePremium();
				updateTarifField();
			}
		});
		form.addComponent(motorBrandField);

		tarifDataSource = new ObjectProperty<>(new BigDecimal(0));
		tarifField = new Label(tarifDataSource);
		tarifField.setCaption("Тариф");
		tarifField.setConverter(lookup(StringToPercentConverter.class));
		form.addComponent(tarifField);

		motorModelField = new EditField("Модель техники", "Введите модель техники");
		motorModelField.setRequired(true);
		motorModelField.setColumns(13);
		form.addComponent(motorModelField);

		riskSumField = new EditField("Страховая сумма", "Введите сумму возмещения в рублях");
		riskSumField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				calculatePremium();
			}
		});
		riskSumField.setRequired(true);
		form.addComponent(riskSumField);

		// По умолчанию страховка на год.
		if (obj.getCoverTime() == null)
			obj.setCoverTime(Insurance.PeriodOfCover.YEAR);
		coverTimeField = new OptionGroup("Срок страхования");
		coverTimeField.setDescription("Укажите требуемый срок действия страхового полиса");
		coverTimeField.setRequired(true);
		coverTimeField.setNullSelectionAllowed(false);
		coverTimeField.setNewItemsAllowed(false);
		coverTimeField.setImmediate(true);
		ComponentUtil.fillSelectByEnum(coverTimeField, Insurance.PeriodOfCover.class);
		coverTimeField.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				calculatePremium();
				calcEndDate();
				updateTarifField();
			}
		});
		form.addComponent(coverTimeField);

		premiumField = new EditField("Страховая премия", "Введите стоимость страховки в рублях");
		premiumField.setRequired(true);
		form.addComponent(premiumField);

		paymentDateField = new LocalDateField("Дата оплаты", "Введите дату оплаты страховой премии");
		paymentDateField.setRequired(true);
		paymentDateField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				// пересчитать дату начала действия договора
				final LocalDate newDate = (LocalDate) paymentDateField.getConvertedValue();
				final LocalDate startDate = ((LocalDate) startDateField.getConvertedValue());
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
			public void valueChange(final ValueChangeEvent event) {
				calcEndDate();

			}
		});
		form.addComponent(startDateField);

		endDateField = new LocalDateField("Дата окончания договора", "Введите дату окончания ");
		endDateField.setRequired(true);
		form.addComponent(endDateField);

		dealerField = new SalePointSelect("Точка продажи", "Название автосоалона где продана страховка", null);
		// dealerField.setRequired(true);
		form.addComponent(dealerField);

		return form;
	}

	private void updateTarifField() {
		if (motorBrandField.getPropertyDataSource() != null &&
				coverTimeField.getContainerDataSource() != null) {
			final Insurance.PeriodOfCover coverPeriod = (Insurance.PeriodOfCover) coverTimeField.getConvertedValue();
			final String motorBrand = (String) motorBrandField.getValue();
			if (coverPeriod != null && motorBrand != null) {
				final InsuranceCalculator calc = lookup(InsuranceCalculator.class);
				final BigDecimal premium = calc.findTarif(motorBrand, coverPeriod);
				tarifDataSource.setValue(premium);
			}
		}
	}

	private void calcEndDate() {
		// пересчитать дату окончания договора
		final LocalDate newDate = (LocalDate) startDateField.getConvertedValue();
		if (newDate != null && endDateField.getPropertyDataSource() != null) {
			final Insurance.PeriodOfCover coverPeriod = (Insurance.PeriodOfCover) coverTimeField.getConvertedValue();
			if (coverPeriod == Insurance.PeriodOfCover.YEAR)
				endDateField.setConvertedValue(newDate.plusYears(1).minusDays(1));
			else
				endDateField.setConvertedValue(newDate.plusMonths(6).minusDays(1));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.web.commons.window.AbstractEditForm#initObject(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void initObject(final Insurance obj) {
		if (obj.getId() == null) {
			final LocalDate now = LocalDate.now();
			obj.setDate(now);
			obj.setPaymentDate(now);
			obj.setCoverTime(Insurance.PeriodOfCover.YEAR);
			obj.setStartDate(obj.getPaymentDate().plusDays(1));
			obj.setEndDate(obj.getStartDate().plusYears(1).minusDays(1));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.web.commons.window.AbstractEditForm#saveObject(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void saveObject(final Insurance obj) {
		lookup(InsuranceService.class).saveAndIssue(obj);
		Notification.show("Полис сохранен", Notification.Type.TRAY_NOTIFICATION);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ru.extas.web.commons.window.AbstractEditForm#checkBeforeSave(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void checkBeforeSave(final Insurance obj) {
	}

	/**
	 *
	 */
	private void calculatePremium() {
		if (motorBrandField.getPropertyDataSource() != null &&
				premiumField.getPropertyDataSource() != null &&
				coverTimeField.getContainerDataSource() != null) {
			final Insurance.PeriodOfCover coverPeriod = (Insurance.PeriodOfCover) coverTimeField.getConvertedValue();
			final BigDecimal riskSum = (BigDecimal) riskSumField.getConvertedValue();
			final String motorBrand = (String) motorBrandField.getValue();
			if (riskSum != null && motorBrand != null) {
				final InsuranceCalculator calc = lookup(InsuranceCalculator.class);
				final BigDecimal premium = calc.calcPropInsPremium(new Insurance(motorBrand, riskSum, coverPeriod));
				premiumField.setConvertedValue(premium);
			}
		}
	}

}
