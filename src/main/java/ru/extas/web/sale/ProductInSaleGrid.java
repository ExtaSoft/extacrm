package ru.extas.web.sale;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import ru.extas.model.sale.ProductInSale;
import ru.extas.model.sale.Sale;
import ru.extas.web.commons.ExtaTheme;
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
        setWidth(500, Unit.PIXELS);
        setHeight(200, Unit.PIXELS);
        setCaption(caption);
	}

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {
		final VerticalLayout fieldLayout = new VerticalLayout();
        fieldLayout.setSizeFull();
        fieldLayout.setSpacing(true);
        fieldLayout.setMargin(new MarginInfo(true, false, true, false));

        if (!isReadOnly()) {
            final MenuBar commandBar = new MenuBar();
            commandBar.setAutoOpen(true);
            commandBar.addStyleName(ExtaTheme.GRID_TOOLBAR);
            commandBar.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);
            commandBar.addStyleName(ExtaTheme.MENUBAR_SMALL);

            final MenuBar.MenuItem addProdBtn = commandBar.addItem("Добавить", event -> {

                final ProductInSale productInSale = new ProductInSale(sale);
                final ProdInSaleEditForm editWin = new ProdInSaleEditForm("Новый продукт в продаже", productInSale);
                editWin.addCloseFormListener(event1 -> {
                    if (editWin.isSaved()) {
                        container.addBean(productInSale);
                    }
                });
                FormUtils.showModalWin(editWin);
            });
			addProdBtn.setDescription("Добавить продукт в продажу");
			addProdBtn.setIcon(Fontello.DOC_NEW);

			final MenuBar.MenuItem edtProdBtn = commandBar.addItem("Изменить", event -> {
                if (productTable.getValue() != null) {
                    final BeanItem<ProductInSale> prodItem = (BeanItem<ProductInSale>) productTable.getItem(productTable.getValue());
                    final ProdInSaleEditForm editWin = new ProdInSaleEditForm("Редактирование продукта в продаже", prodItem.getBean());
                    FormUtils.showModalWin(editWin);
                }
            });
			edtProdBtn.setDescription("Изменить выделенный в списке продукт");
			edtProdBtn.setIcon(Fontello.EDIT_3);

			final MenuBar.MenuItem delProdBtn = commandBar.addItem("Удалить", event -> {
                if (productTable.getValue() != null) {
                    productTable.removeItem(productTable.getValue());
                }
            });
			delProdBtn.setDescription("Удалить продукт из продажи");
			delProdBtn.setIcon(Fontello.TRASH);

			fieldLayout.addComponent(commandBar);
		}

		productTable = new Table();
        productTable.setSizeFull();
        productTable.addStyleName(ExtaTheme.TABLE_SMALL);
        productTable.addStyleName(ExtaTheme.TABLE_COMPACT);
        productTable.setRequired(true);
		productTable.setSelectable(true);
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
		productTable.addItemSetChangeListener(event -> setValue(newArrayList(productTable.getItemIds())));
		// Колонки таблицы
		productTable.setVisibleColumns("product.name", "summ", "period");
		productTable.setColumnHeader("product.name", "Продукт");
		productTable.setColumnHeader("summ", "Сумма");
		productTable.setColumnHeader("period", "Срок");
		fieldLayout.addComponent(productTable);
        fieldLayout.setExpandRatio(productTable, 1);

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
