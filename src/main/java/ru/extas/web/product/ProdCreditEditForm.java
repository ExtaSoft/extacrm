package ru.extas.web.product;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import ru.extas.model.ProdCredit;
import ru.extas.server.ProdCreditRepository;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.converters.StringToPercentConverter;
import ru.extas.web.commons.window.AbstractEditForm;
import ru.extas.web.contacts.CompanySelect;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Панель ввода/редактирования кредитного продукта
 *
 * @author Valery Orlov
 *         Date: 20.01.14
 *         Time: 18:27
 */
public class ProdCreditEditForm extends AbstractEditForm<ProdCredit> {

	// Компоненты редактирования
	@PropertyId("name")
	private EditField nameField;
	@PropertyId("vendor")
	private CompanySelect vendorField;
	@PropertyId("percent")
	private EditField percentField;
	@PropertyId("maxPeroid")
	private EditField maxPeroidField;
	@PropertyId("minDownpayment")
	private EditField minDownpaymentField;
	@PropertyId("maxSum")
	private EditField maxSumField;
	@PropertyId("active")
	private CheckBox activeField;
	@PropertyId("comment")
	private TextArea commentField;

	public ProdCreditEditForm(final String caption, final BeanItem<ProdCredit> obj) {
		super(caption, obj);
	}

	@Override
	protected void initObject(final ProdCredit obj) {
		if (obj.getId() == null) {
			obj.setActive(true);
		}
	}

	@Override
	protected void saveObject(ProdCredit obj) {
		ProdCredit loc = lookup(ProdCreditRepository.class).save(obj);
		obj = loc;
	}

	@Override
	protected void checkBeforeSave(final ProdCredit obj) {

	}

	@Override
	protected ComponentContainer createEditFields(final ProdCredit obj) {
		final FormLayout form = new FormLayout();

		nameField = new EditField("Название продукта", "Введите название продукта");
		nameField.setColumns(30);
		nameField.setRequired(true);
		form.addComponent(nameField);

		vendorField = new CompanySelect("Банк");
		form.addComponent(vendorField);

		percentField = new EditField("Процент по кредиту", "Введите кредитный процент по кредитному продукту");
		percentField.setRequired(true);
		percentField.setConverter(lookup(StringToPercentConverter.class));
		form.addComponent(percentField);

		maxPeroidField = new EditField("Max период кредитования", "Введите максимальный период кредитования по продукту");
		maxPeroidField.setRequired(true);
		form.addComponent(maxPeroidField);

		minDownpaymentField = new EditField("Первоначальный взнос", "Введите минимальный первоначальный взнос по кредиту");
		minDownpaymentField.setRequired(true);
		minDownpaymentField.setConverter(lookup(StringToPercentConverter.class));
		form.addComponent(minDownpaymentField);

		maxSumField = new EditField("Сумма кредита", "Введите максимальную сумму кредита по программе");
		maxSumField.setRequired(true);
		form.addComponent(maxSumField);

		activeField = new CheckBox("Активный продукт");
		activeField.setDescription("Участвует ли продукт в продажах (учавствует если активен)");
		form.addComponent(activeField);

		commentField = new TextArea("Примечание");
		commentField.setDescription("Дополнительная информация о продукте");
		commentField.setNullRepresentation("");
		commentField.setInputPrompt("Дополнительная информация о продукте");
		commentField.setColumns(30);
		commentField.setRows(5);
		form.addComponent(commentField);

		return form;
	}

}
