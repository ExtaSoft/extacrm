package ru.extas.web.sale;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import ru.extas.model.sale.Sale;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.component.ExtaCustomField;

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
public class SaleField extends ExtaCustomField<Sale> {

    private BeanItem<Sale> saleItem;
    private ProductInstancesField productInstancesField;

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
        super(caption, "");
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
                    ((VerticalLayout) getContent()).replaceComponent(productInstancesField, productInstancesField = createProdInSale(sale));
            });
            FormUtils.showModalWin(form);
        });
        openBtn.addStyleName(ExtaTheme.BUTTON_LINK);
                container.addComponent(openBtn);
        // Список продуктов в продаже
        productInstancesField = createProdInSale(sale);
        container.addComponent(productInstancesField);

        return container;
    }

    private ProductInstancesField createProdInSale(final Sale sale) {
        final ProductInstancesField instancesField = new ProductInstancesField("Продукты в продаже", null, getValue(), null, null, null);
        instancesField.setReadOnly(true);
        instancesField.setPropertyDataSource(saleItem.getItemProperty("productInstances"));
        return instancesField;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Sale> getType() {
        return Sale.class;
    }
}
