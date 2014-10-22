package ru.extas.web.product;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import ru.extas.model.sale.ProdInsurance;
import ru.extas.server.sale.ProdInsuranceRepository;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.converters.StringToPercentConverter;
import ru.extas.web.contacts.company.CompanySelect;

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
public class ProdInsuranceEditForm extends ExtaEditForm<ProdInsurance> {

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

    public ProdInsuranceEditForm(final ProdInsurance prodInsurance) {
        super(prodInsurance.isNew() ? "Новая страховая программа" : "Редактировать страховую программу", new BeanItem(prodInsurance));
    }

    /** {@inheritDoc} */
	@Override
	protected void initObject(final ProdInsurance obj) {
		if (obj.isNew()) {
			obj.setActive(true);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected ProdInsurance saveObject(ProdInsurance obj) {
        obj = lookup(ProdInsuranceRepository.class).save(obj);
        NotificationUtil.showSuccess("Продукт сохранен");
        return obj;
    }

    /** {@inheritDoc} */
	@Override
	protected ComponentContainer createEditFields(final ProdInsurance obj) {
		final FormLayout form = new ExtaFormLayout();

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
		commentField.setRows(5);
		form.addComponent(commentField);

		return form;
	}

}
