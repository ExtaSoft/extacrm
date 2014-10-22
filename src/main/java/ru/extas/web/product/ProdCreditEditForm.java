package ru.extas.web.product;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import ru.extas.model.sale.ProdCredit;
import ru.extas.server.sale.ProdCreditRepository;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.converters.StringToPercentConverter;
import ru.extas.web.contacts.company.CompanyField;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Панель ввода/редактирования кредитного продукта
 *
 * @author Valery Orlov
 *         Date: 20.01.14
 *         Time: 18:27
 * @version $Id: $Id
 * @since 0.3
 */
public class ProdCreditEditForm extends ExtaEditForm<ProdCredit> {

	// Компоненты редактирования
	@PropertyId("name")
	private EditField nameField;

	@PropertyId("vendor")
	private CompanyField vendorField;

	@PropertyId("programType")
	private ProdCredProgSelect programTypeField;

	@PropertyId("minSum")
	private EditField minSumField;

	@PropertyId("maxSum")
	private EditField maxSumField;

	@PropertyId("minDownpayment")
	private EditField minDownpaymentField;

	@PropertyId("maxDownpayment")
	private EditField maxDownpaymentField;

	@PropertyId("minPeriod")
	private EditField minPeriodField;

	@PropertyId("maxPeriod")
	private EditField maxPeriodField;

	@PropertyId("percents")
	private ProdCredPercentField percentsField;

	@PropertyId("step")
	private EditField stepField;

	@PropertyId("dealerSubsidy")
	private EditField dealerSubsidyField;

	@PropertyId("docList")
	private ProdCredDocsField docListField;

	@PropertyId("active")
	private CheckBox activeField;

	@PropertyId("comment")
	private TextArea commentField;

    public ProdCreditEditForm(final ProdCredit prodCredit) {
        super(prodCredit.isNew() ? "Новый продукт" : "Редактировать продукт", new BeanItem(prodCredit));
    }

    /** {@inheritDoc} */
	@Override
	protected void initObject(final ProdCredit obj) {
		if (obj.isNew()) {
			obj.setActive(true);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected ProdCredit saveObject(final ProdCredit obj) {
		final ProdCredit loc = lookup(ProdCreditRepository.class).save(obj);
        NotificationUtil.showSuccess("Продукт сохранен");
        return loc;
    }

    /** {@inheritDoc} */
	@Override
	protected ComponentContainer createEditFields(final ProdCredit obj) {
		final FormLayout form = new ExtaFormLayout();

		activeField = new CheckBox("Активный продукт");
		activeField.setDescription("Укажите участвует ли продукт в продажах (учавствует если активен)");
		form.addComponent(activeField);

		nameField = new EditField("Название продукта", "Введите название продукта");
		nameField.setColumns(30);
		nameField.setRequired(true);
		form.addComponent(nameField);

		vendorField = new CompanyField("Банк");
		vendorField.setWidth(30, Unit.EM);
		form.addComponent(vendorField);

		programTypeField = new ProdCredProgSelect("Тип программы", "Выберите тип кредитной программы");
		form.addComponent(programTypeField);

		minSumField = new EditField("Сумма кредита(min)", "Введите минимальную сумму кредита по программе");
		minSumField.setRequired(true);
		form.addComponent(minSumField);

		maxSumField = new EditField("Сумма кредита(max)", "Введите максимальную сумму кредита по программе");
		maxSumField.setRequired(true);
		form.addComponent(maxSumField);

		minDownpaymentField = new EditField("Первоначальный взнос(min)", "Введите минимальный первоначальный взнос по кредиту (%)");
		minDownpaymentField.setRequired(true);
		minDownpaymentField.setConverter(lookup(StringToPercentConverter.class));
		form.addComponent(minDownpaymentField);

		maxDownpaymentField = new EditField("Первоначальный взнос(max)", "Введите максимальный первоначальный взнос по кредиту (%)");
		maxDownpaymentField.setRequired(true);
		maxDownpaymentField.setConverter(lookup(StringToPercentConverter.class));
		form.addComponent(maxDownpaymentField);

		minPeriodField = new EditField("Период кредитования(min)", "Введите минимальный период кредитования по продукту");
		minPeriodField.setRequired(true);
		form.addComponent(minPeriodField);

		maxPeriodField = new EditField("Период кредитования(max)", "Введите максимальный период кредитования по продукту");
		maxPeriodField.setRequired(true);
		form.addComponent(maxPeriodField);

		percentsField = new ProdCredPercentField("Процентные ставки", "Введите процентные ставки по кредиту в зависимости от начальных параментов кредита", obj);
		percentsField.setRequired(true);
		form.addComponent(percentsField);

		stepField = new EditField("Шаг кредита(мес.)", "Введите шаг с которым может увеличиваться период кредитования по продукту");
		stepField.setRequired(true);
		form.addComponent(stepField);

		dealerSubsidyField = new EditField("Субсидия дилера", "Введите процент субсидии дилера (процент от суммы кредита)");
		dealerSubsidyField.setRequired(true);
		dealerSubsidyField.setConverter(lookup(StringToPercentConverter.class));
		form.addComponent(dealerSubsidyField);

		docListField = new ProdCredDocsField("Кмплект документов", "Введите комплект докуметнов по продукту (обязательные и на выбор", obj);
		docListField.setRequired(true);
		form.addComponent(docListField);

		commentField = new TextArea("Примечание");
		commentField.setDescription("Введите дополнительную информацию о продукте");
		commentField.setNullRepresentation("");
		commentField.setInputPrompt("Дополнительная информация о продукте");
		commentField.setRows(5);
		form.addComponent(commentField);

		return form;
	}

}
