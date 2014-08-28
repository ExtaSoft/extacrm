package ru.extas.web.product;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import ru.extas.model.sale.ProdCreditPercent;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.converters.StringToPercentConverter;
import ru.extas.web.commons.ExtaEditForm;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода/редактирования процентной ставки в кредитном продукте
 *
 * @author Valery Orlov
 *         Date: 07.02.14
 *         Time: 19:17
 * @version $Id: $Id
 * @since 0.3
 */
public class ProdCreditPercentForm extends ExtaEditForm<ProdCreditPercent> {

	@PropertyId("percent")
	private EditField percentField;
	@PropertyId("period")
	private EditField periodField;
	@PropertyId("downpayment")
	private EditField downpaymentField;

	/**
	 * <p>Constructor for ProdCreditPercentForm.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param beanItem a {@link com.vaadin.data.util.BeanItem} object.
	 */
	protected ProdCreditPercentForm(final String caption, final BeanItem<ProdCreditPercent> beanItem) {
		super(caption, beanItem);
	}

	/** {@inheritDoc} */
	@Override
	protected void initObject(final ProdCreditPercent obj) {

	}

	/** {@inheritDoc} */
	@Override
	protected ProdCreditPercent saveObject(final ProdCreditPercent obj) {
        NotificationUtil.showSuccess("Процентная ставка сохранена");
        return obj;
    }

	/** {@inheritDoc} */
	@Override
	protected void checkBeforeSave(final ProdCreditPercent obj) {

	}

	/** {@inheritDoc} */
	@Override
	protected ComponentContainer createEditFields(final ProdCreditPercent obj) {
		final FormLayout form = new ExtaFormLayout();

		percentField = new EditField("Процентная ставка", "Введите процентную ставку");
		percentField.setConverter(lookup(StringToPercentConverter.class));
		form.addComponent(percentField);

		periodField = new EditField("Срок кредитования", "Введите срок крединования при котором приметяется эта проценнтная ставка");
		form.addComponent(periodField);

		downpaymentField = new EditField("Первоначальный взнос", "Введите минимальный первоначальный взнос при котором применяется эта процентная ставка");
		downpaymentField.setConverter(lookup(StringToPercentConverter.class));
		form.addComponent(downpaymentField);

		return form;
	}
}
