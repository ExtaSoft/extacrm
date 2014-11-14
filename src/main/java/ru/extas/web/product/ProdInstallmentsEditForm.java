package ru.extas.web.product;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import ru.extas.model.sale.ProdInstallments;
import ru.extas.server.sale.ProdInstallmentsRepository;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.converters.StringToPercentConverter;
import ru.extas.web.contacts.company.CompanyField;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Панель ввода/редактирования рассрочки
 *
 * @author Valery Orlov
 *         Date: 20.01.14
 *         Time: 18:27
 * @version $Id: $Id
 * @since 0.3
 */
public class ProdInstallmentsEditForm extends ExtaEditForm<ProdInstallments> {

	// Компоненты редактирования
	@PropertyId("name")
	private EditField nameField;
	@PropertyId("vendor")
	private CompanyField vendorField;
	@PropertyId("maxPeroid")
	private EditField maxPeroidField;
	@PropertyId("minDownpayment")
	private EditField minDownpaymentField;
	@PropertyId("active")
	private CheckBox activeField;
	@PropertyId("comment")
	private TextArea commentField;

    public ProdInstallmentsEditForm(final ProdInstallments prodInstallments) {
        super(prodInstallments.isNew() ?
                "Новый продукт \"Рассрочка\"" :
                "Редактировать продукт",
                prodInstallments);
    }

    /** {@inheritDoc} */
	@Override
	protected void initEntity(final ProdInstallments installments) {
		if (installments.isNew()) {
			installments.setActive(true);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected ProdInstallments saveEntity(ProdInstallments installments) {
        installments = lookup(ProdInstallmentsRepository.class).save(installments);
        NotificationUtil.showSuccess("Продукт сохранен");
        return installments;
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

		vendorField = new CompanyField("Поставщик");
		form.addComponent(vendorField);

		maxPeroidField = new EditField("Max период рассрочки", "Введите максимальный период рассрочки по продукту");
		maxPeroidField.setRequired(true);
		form.addComponent(maxPeroidField);

		minDownpaymentField = new EditField("Первоначальный взнос", "Введите минимальный первоначальный взнос");
		minDownpaymentField.setRequired(true);
		minDownpaymentField.setConverter(lookup(StringToPercentConverter.class));
		form.addComponent(minDownpaymentField);

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
