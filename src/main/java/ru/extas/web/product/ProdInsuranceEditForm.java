package ru.extas.web.product;

import com.vaadin.data.fieldgroup.PropertyId;
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
public class ProdInsuranceEditForm extends ExtaEditForm<ProdInsurance> {

	// Компоненты редактирования
	@PropertyId("name")
	private EditField nameField;
	@PropertyId("vendor")
	private CompanyField vendorField;
	@PropertyId("percent")
	private EditField percentField;
	@PropertyId("active")
	private CheckBox activeField;
	@PropertyId("comment")
	private TextArea commentField;

    public ProdInsuranceEditForm(final ProdInsurance prodInsurance) {
        super(prodInsurance.isNew() ? "Новая страховая программа" : "Редактировать страховую программу", prodInsurance);
    }

    /** {@inheritDoc} */
	@Override
	protected void initEntity(final ProdInsurance prodInsurance) {
		if (prodInsurance.isNew()) {
			prodInsurance.setActive(true);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected ProdInsurance saveEntity(ProdInsurance prodInsurance) {
        prodInsurance = lookup(ProdInsuranceRepository.class).save(prodInsurance);
        NotificationUtil.showSuccess("Продукт сохранен");
        return prodInsurance;
    }

    /** {@inheritDoc} */
	@Override
	protected ComponentContainer createEditFields() {
		final FormLayout form = new ExtaFormLayout();
        form.setSizeFull();

		nameField = new EditField("Название продукта", "Введите название продукта");
		nameField.setColumns(30);
		nameField.setRequired(true);
		form.addComponent(nameField);

		vendorField = new CompanyField("Страховщик");
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
