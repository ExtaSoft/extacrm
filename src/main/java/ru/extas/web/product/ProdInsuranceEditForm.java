package ru.extas.web.product;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import ru.extas.model.ProdInsurance;
import ru.extas.server.ProdInsuranceRepository;
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
public class ProdInsuranceEditForm extends AbstractEditForm<ProdInsurance> {

	// Компоненты редактирования
	@PropertyId("name")
	private EditField nameField;
	@PropertyId("vendor")
	private CompanySelect vendorField;
	@PropertyId("percent")
	private EditField percentField;
	@PropertyId("active")
	private CheckBox activeField;
	@PropertyId("comment")
	private TextArea commentField;

	public ProdInsuranceEditForm(final String caption, final BeanItem<ProdInsurance> obj) {
		super(caption, obj);
	}

	@Override
	protected void initObject(final ProdInsurance obj) {
		if (obj.getId() == null) {
			obj.setActive(true);
		}
	}

	@Override
	protected void saveObject(final ProdInsurance obj) {
		lookup(ProdInsuranceRepository.class).save(obj);
	}

	@Override
	protected void checkBeforeSave(final ProdInsurance obj) {

	}

	@Override
	protected ComponentContainer createEditFields(final ProdInsurance obj) {
		final FormLayout form = new FormLayout();

		nameField = new EditField("Название продукта", "Введите название продукта");
		nameField.setColumns(30);
		nameField.setRequired(true);
		form.addComponent(nameField);

		vendorField = new CompanySelect("Страховщик");
		form.addComponent(vendorField);

		percentField = new EditField("Процент страх.премии", "Введите процент страховой премии по продукту");
		percentField.setRequired(true);
		percentField.setConverter(lookup(StringToPercentConverter.class));
		form.addComponent(percentField);

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
