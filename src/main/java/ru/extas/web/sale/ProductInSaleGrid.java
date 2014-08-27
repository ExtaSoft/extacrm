package ru.extas.web.sale;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import ru.extas.model.sale.ProductInSale;
import ru.extas.model.sale.Sale;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.FormUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Встроенная форма редактирования продуктов в продаже
 *
 * @author Valery Orlov
 *         Date: 21.01.14
 *         Time: 15:28
 * @version $Id: $Id
 * @since 0.3
 */
public class ProductInSaleGrid extends CustomField<List> {

	private final Sale sale;
	private Table productTable;
	private BeanItemContainer<ProductInSale> container;

	/**
	 * <p>Constructor for ProductInSaleGrid.</p>
	 *
	 * @param sale a {@link ru.extas.model.sale.Sale} object.
	 */
	public ProductInSaleGrid(final Sale sale) {
		this("Продукты в продаже", sale);
	}

	/**
	 * <p>Constructor for ProductInSaleGrid.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param sale a {@link ru.extas.model.sale.Sale} object.
	 */
	public ProductInSaleGrid(final String caption, final Sale sale) {
		this.sale = sale;
		setCaption(caption);
	}

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {
		final VerticalLayout fieldLayout = new VerticalLayout();
		fieldLayout.setSpacing(true);

		if (!isReadOnly()) {
			final HorizontalLayout commandBar = new HorizontalLayout();
			commandBar.addStyleName("configure");
			commandBar.setSpacing(true);

			final Button addProdBtn = new Button("Добавить", new Button.ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final Button.ClickEvent event) {
					final BeanItem<ProductInSale> newObj = new BeanItem<>(new ProductInSale(sale));

					final ProdInSaleEditForm editWin = new ProdInSaleEditForm("Новый продукт в продаже", newObj);
                    editWin.addCloseFormListener(new ExtaEditForm.CloseFormListener() {
                        @Override
                        public void closeForm(ExtaEditForm.CloseFormEvent event) {
                        if (editWin.isSaved()) {
                            container.addBean(newObj.getBean());
                        }
                    }
                });
                    FormUtils.showModalWin(editWin);
				}
			});
			addProdBtn.setDescription("Добавить продукт в продажу");
			addProdBtn.setIcon(Fontello.DOC_NEW);
			commandBar.addComponent(addProdBtn);

			final Button edtProdBtn = new Button("Изменить", new Button.ClickListener() {
				@Override
				public void buttonClick(final Button.ClickEvent event) {
					if (productTable.getValue() != null) {
						final BeanItem<ProductInSale> prodItem = (BeanItem<ProductInSale>) productTable.getItem(productTable.getValue());
						final ProdInSaleEditForm editWin = new ProdInSaleEditForm("Редактирование продукта в продаже", prodItem);
                        FormUtils.showModalWin(editWin);
					}
				}
			});
			edtProdBtn.setDescription("Изменить выделенный в списке продукт");
			edtProdBtn.setIcon(Fontello.EDIT_3);
			commandBar.addComponent(edtProdBtn);

			final Button delProdBtn = new Button("Удалить", new Button.ClickListener() {
				@Override
				public void buttonClick(final Button.ClickEvent event) {
					if (productTable.getValue() != null) {
						productTable.removeItem(productTable.getValue());
					}
				}
			});
			delProdBtn.setDescription("Удалить продукт из продажи");
			delProdBtn.setIcon(Fontello.TRASH);
			commandBar.addComponent(delProdBtn);

			fieldLayout.addComponent(commandBar);
		}

		productTable = new Table();
		productTable.setRequired(true);
		productTable.setSelectable(true);
		productTable.setHeight(10, Unit.EM);
		productTable.setWidth(35, Unit.EM);
		final Property dataSource = getPropertyDataSource();
		final List<ProductInSale> productInSaleList = dataSource != null ? (List<ProductInSale>) dataSource.getValue() : sale.getProductInSales();
		container = new BeanItemContainer<>(ProductInSale.class);
		container.addNestedContainerProperty("product.name");
		if (productInSaleList != null) {
			for (final ProductInSale productInSale : productInSaleList) {
				container.addBean(productInSale);
			}
		}
		productTable.setContainerDataSource(container);
		productTable.addItemSetChangeListener(new Container.ItemSetChangeListener() {
			@Override
			public void containerItemSetChange(final Container.ItemSetChangeEvent event) {
				setValue(newArrayList(productTable.getItemIds()));
			}
		});
		// Колонки таблицы
		productTable.setVisibleColumns("product.name", "summ", "period");
		productTable.setColumnHeader("product.name", "Продукт");
		productTable.setColumnHeader("summ", "Сумма");
		productTable.setColumnHeader("period", "Срок");
		fieldLayout.addComponent(productTable);

		return fieldLayout;
	}

	/** {@inheritDoc} */
	@Override
	public void commit() throws SourceException, Validator.InvalidValueException {
		super.commit();
		final Property dataSource = getPropertyDataSource();
		if (dataSource != null)
			dataSource.setValue(container.getItemIds());
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends List> getType() {
		return List.class;
	}
}
