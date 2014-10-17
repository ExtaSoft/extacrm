package ru.extas.web.lead;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.lead.Lead;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.FormUtils;

/**
 * Поле расширенного просмотра лида.
 *
 * @author Valery Orlov
 *         Date: 05.02.14
 *         Time: 10:59
 * @version $Id: $Id
 * @since 0.3
 */
public class LeadField extends CustomField<Lead> {

	private BeanItem<Lead> leadItem;

	/**
	 * <p>Constructor for LeadField.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 */
	public LeadField(final String caption) {
		setCaption(caption);
	}

	/**
	 * <p>Constructor for LeadField.</p>
	 */
	public LeadField() {
		this("Лид");
	}

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {
		final VerticalLayout container = new VerticalLayout();
		container.setSpacing(true);

		final Lead lead = (Lead) getPropertyDataSource().getValue();
		leadItem = new BeanItem<>(lead != null ? lead : new Lead());

		// Открытие формы ввода/редактирования
		final Button.ClickListener openLeadFormListener = event -> {
            final LeadEditForm form = new LeadEditForm(lead, false);
FormUtils.showModalWin(form);
        };
		final Button openBtn = new Button("Нажмите для просмотра/редактирования лида...", openLeadFormListener);
		openBtn.addStyleName(ExtaTheme.BUTTON_LINK);
                container.addComponent(openBtn);

		final HorizontalLayout fieldsContainer = new HorizontalLayout();
		fieldsContainer.setSpacing(true);
		fieldsContainer.addStyleName(ExtaTheme.BORDERED_ITEMS);
		// Тип техники
		final Label motorTypeField = new Label(leadItem.getItemProperty("motorType"));
		motorTypeField.setCaption("Тип техники");
		fieldsContainer.addComponent(motorTypeField);
		// Бренд техники
		final Label motorBrandField = new Label(leadItem.getItemProperty("motorBrand"));
		motorBrandField.setCaption("Марка");
		fieldsContainer.addComponent(motorBrandField);
		// Модель техники
		final Label motorModelField = new Label(leadItem.getItemProperty("motorModel"));
		motorModelField.setCaption("Марка");
		fieldsContainer.addComponent(motorModelField);
		// Стоимость
		final Label sumField = new Label(leadItem.getItemProperty("motorPrice"));
		sumField.setCaption("Сумма");
		fieldsContainer.addComponent(sumField);

		container.addComponent(fieldsContainer);
		return container;
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends Lead> getType() {
		return Lead.class;
	}
}
