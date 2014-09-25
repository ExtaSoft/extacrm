package ru.extas.web.sale;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.sale.Sale;
import ru.extas.web.commons.ExtaEditForm;
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
    private ProductInSaleGrid productInSaleField;

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
    public SaleField(String caption) {
        setCaption(caption);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {

        final Sale sale = (Sale) getPropertyDataSource().getValue();
        saleItem = new BeanItem<>(sale);

        VerticalLayout container = new VerticalLayout();
        container.setSpacing(true);

        // Открытие формы ввода/редактирования
        Button openBtn = new Button("Нажмите для просмотра/редактирования продажи...", event -> {
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

    private ProductInSaleGrid createProdInSale(final Sale sale) {
        ProductInSaleGrid productInSale = new ProductInSaleGrid("Продукты в продаже", sale);
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
