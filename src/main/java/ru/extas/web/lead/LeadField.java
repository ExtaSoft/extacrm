package ru.extas.web.lead;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.Lead;

/**
 * Поле расширенного просмотра лида.
 *
 * @author Valery Orlov
 *         Date: 05.02.14
 *         Time: 10:59
 */
public class LeadField extends CustomField<Lead> {

	private BeanItem<Lead> leadItem;

	public LeadField(String caption) {
		setCaption(caption);
	}

	public LeadField() {
		this("Лид");
	}

	@Override
	protected Component initContent() {
		VerticalLayout container = new VerticalLayout();
		container.setSpacing(true);

		Lead lead = (Lead) getPropertyDataSource().getValue();
		leadItem = new BeanItem<>(lead != null ? lead : new Lead());

		// Открытие формы ввода/редактирования
		final Button.ClickListener openLeadFormListener = new Button.ClickListener() {
			@Override
			public void buttonClick(final Button.ClickEvent event) {
				LeadEditForm form = new LeadEditForm("Просмотр/редактирование лида", leadItem, false);
				form.showModal();
			}
		};
		Button openBtn = new Button("Нажмите для просмотра/редактирования лида...", openLeadFormListener);
		openBtn.addStyleName("link");
		container.addComponent(openBtn);

		HorizontalLayout fieldsContainer = new HorizontalLayout();
		fieldsContainer.setSpacing(true);
		fieldsContainer.addStyleName("bordered-items");
		// Тип техники
		Label motorTypeField = new Label(leadItem.getItemProperty("motorType"));
		motorTypeField.setCaption("Тип техники");
		fieldsContainer.addComponent(motorTypeField);
		// Бренд техники
		Label motorBrandField = new Label(leadItem.getItemProperty("motorBrand"));
		motorBrandField.setCaption("Марка");
		fieldsContainer.addComponent(motorBrandField);
		// Модель техники
		Label motorModelField = new Label(leadItem.getItemProperty("motorModel"));
		motorModelField.setCaption("Марка");
		fieldsContainer.addComponent(motorModelField);
		// Стоимость
		Label sumField = new Label(leadItem.getItemProperty("motorPrice"));
		sumField.setCaption("Сумма");
		fieldsContainer.addComponent(sumField);

		container.addComponent(fieldsContainer);
		return container;
	}

	@Override
	public Class<? extends Lead> getType() {
		return Lead.class;
	}
}
