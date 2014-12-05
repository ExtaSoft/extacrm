package ru.extas.web.sale;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;
import ru.extas.model.sale.Sale;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.FormUtils;

/**
 * Поле расширенного просмотра продажи.
 * Показывает список продуктов в продаже.
 *
 * @author Valery Orlov
 *         Date: 05.02.14
 *         Time: 11:42
 * @version $Id: $Id
 * @since 0.3
 */
public class SaleField extends CustomField<Sale> {

    private BeanItem<Sale> saleItem;
    private ProductInSaleField productInSaleField;

    /**
     * <p>Constructor for SaleField.</p>
     */
    public SaleField() {
        this("Продажа");
    }

    /**
     * <p>Constructor for SaleField.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public SaleField(final String caption) {
        setCaption(caption);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {

        final Sale sale = (Sale) getPropertyDataSource().getValue();
        saleItem = new BeanItem<>(sale);

        final VerticalLayout container = new VerticalLayout();
        container.setSpacing(true);

        // Открытие формы ввода/редактирования
        final Button openBtn = new Button("Нажмите для просмотра/редактирования продажи...", event -> {
            final SaleEditForm form = new SaleEditForm(sale);
            form.addCloseFormListener(event1 -> {
                if (form.isSaved())
                    ((VerticalLayout) getContent()).replaceComponent(productInSaleField, productInSaleField = createProdInSale(sale));
            });
            FormUtils.showModalWin(form);
        });
        openBtn.addStyleName(ExtaTheme.BUTTON_LINK);
                container.addComponent(openBtn);
        // Список продуктов в продаже
        productInSaleField = createProdInSale(sale);
        container.addComponent(productInSaleField);

        return container;
    }

    private ProductInSaleField createProdInSale(final Sale sale) {
        final ProductInSaleField productInSale = new ProductInSaleField("Продукты в продаже", null);
        productInSale.setReadOnly(true);
        productInSale.setPropertyDataSource(saleItem.getItemProperty("productInSales"));
        return productInSale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Sale> getType() {
        return Sale.class;
    }
}
