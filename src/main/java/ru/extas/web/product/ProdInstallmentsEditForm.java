package ru.extas.web.product;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.sale.ProdInstallments;
import ru.extas.server.sale.ProdInstallmentsRepository;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.converters.StringToPercentConverter;
import ru.extas.web.commons.AbstractEditForm;
import ru.extas.web.contacts.CompanySelect;

import static com.google.common.base.Strings.isNullOrEmpty;
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
public class ProdInstallmentsEditForm extends AbstractEditForm<ProdInstallments> {

	// Компоненты редактирования
	@PropertyId("name")
	private EditField nameField;
	@PropertyId("vendor")
	private CompanySelect vendorField;
	@PropertyId("maxPeroid")
	private EditField maxPeroidField;
	@PropertyId("minDownpayment")
	private EditField minDownpaymentField;
	@PropertyId("active")
	private CheckBox activeField;
	@PropertyId("comment")
	private TextArea commentField;

    public ProdInstallmentsEditForm(ProdInstallments prodInstallments) {
        super(isNullOrEmpty(prodInstallments.getId()) ?
                "Новый продукт \"Рассрочка\"" :
                "Редактировать продукт",
                new BeanItem(prodInstallments));
    }

    /** {@inheritDoc} */
	@Override
	protected void initObject(final ProdInstallments obj) {
		if (obj.getId() == null) {
			obj.setActive(true);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void saveObject(final ProdInstallments obj) {
		lookup(ProdInstallmentsRepository.class).save(obj);
        NotificationUtil.showSuccess("Продукт сохранен");
	}

	/** {@inheritDoc} */
	@Override
	protected void checkBeforeSave(final ProdInstallments obj) {

	}

	/** {@inheritDoc} */
	@Override
	protected ComponentContainer createEditFields(final ProdInstallments obj) {
		final FormLayout form = new ExtaFormLayout();

		nameField = new EditField("Название продукта", "Введите название продукта");
		nameField.setColumns(30);
		nameField.setRequired(true);
		form.addComponent(nameField);

		vendorField = new CompanySelect("Поставщик");
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
		commentField.setColumns(30);
		commentField.setRows(5);
		form.addComponent(commentField);

		return form;
	}

}
