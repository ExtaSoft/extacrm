package ru.extas.web.sale;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.Sale;

/**
 * Поле расширенного просмотра продажи.
 * Показывает список продуктов в продаже.
 *
 * @author Valery Orlov
 *         Date: 05.02.14
 *         Time: 11:42
 */
public class SaleField extends CustomField<Sale> {

	private BeanItem<Sale> saleItem;
	private ProductInSaleGrid productInSaleField;

	public SaleField() {
		this("Продажа");
	}

	public SaleField(String caption) {
		setCaption(caption);
	}

	@Override
	protected Component initContent() {

		final Sale sale = (Sale) getPropertyDataSource().getValue();
		saleItem = new BeanItem<>(sale);

		VerticalLayout container = new VerticalLayout();
		container.setSpacing(true);

		// Открытие формы ввода/редактирования
		Button openBtn = new Button("Нажмите для просмотра/редактирования продажи...", new Button.ClickListener() {
			@Override
			public void buttonClick(final Button.ClickEvent event) {
				final SaleEditForm form = new SaleEditForm("Просмотр/редактирование продажи", saleItem);
				form.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(final Window.CloseEvent e) {
						if (form.isSaved())
							((VerticalLayout) getContent()).replaceComponent(productInSaleField, productInSaleField = createProdInSale(sale));
					}
				});
				form.showModal();
			}
		});
		openBtn.addStyleName("link");
		container.addComponent(openBtn);
		// Список продуктов в продаже
		productInSaleField = createProdInSale(sale);
		container.addComponent(productInSaleField);

		return container;
	}

	private ProductInSaleGrid createProdInSale(final Sale sale) {
		ProductInSaleGrid productInSale = new ProductInSaleGrid("Продукты в продаже", sale);
		productInSale.setReadOnly(true);
		productInSale.setPropertyDataSource(saleItem.getItemProperty("productInSales"));
		return productInSale;
	}

	@Override
	public Class<? extends Sale> getType() {
		return Sale.class;
	}
}
