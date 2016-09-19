/**
 *
 */
package ru.extas.web.product;

import com.vaadin.data.util.filter.Compare;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import ru.extas.model.product.Product;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.component.ExtaCustomField;
import ru.extas.web.commons.container.ExtaDbContainer;

/**
 * Компонент выбора продукта
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public abstract class ProductField<TProduct extends Product> extends ExtaCustomField<TProduct> {

    private static final long serialVersionUID = 6004206917183679455L;
    private final Class<TProduct> productCls;

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends TProduct> getType() {
        return productCls;
    }


    public ProductField(final String caption, final String description, final Class<TProduct> productCls) {
        super(caption, description);
        this.productCls = productCls;
    }

    @Override
    protected Component initContent() {
        // A vertical layout with undefined width
        final VerticalLayout box = new VerticalLayout();
        box.setSizeUndefined();

        final ComboBox productSelect = new ComboBox();
        productSelect.setInputPrompt("Выберите продукт...");
        productSelect.setImmediate(true);
        productSelect.setNullSelectionAllowed(false);

        // Инициализация контейнера
        final ExtaDbContainer<TProduct> clientsCont = new ExtaDbContainer<>(productCls);
        clientsCont.addContainerFilter(new Compare.Equal("active", true));
        clientsCont.sort(new Object[]{"name"}, new boolean[]{true});

        // Устанавливаем контент выбора
        productSelect.setFilteringMode(FilteringMode.CONTAINS);
        productSelect.setContainerDataSource(clientsCont);
        productSelect.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        productSelect.setItemCaptionPropertyId("name");
        productSelect.addStyleName(ExtaTheme.COMBOBOX_BORDERLESS);

        productSelect.setPropertyDataSource(getPropertyDataSource());
        productSelect.addValueChangeListener(e -> setValue((TProduct) productSelect.getConvertedValue()));
//        productSelect.setValue(getValue());
        clientsCont.setSingleSelectConverter(productSelect);

        productSelect.setWidth(100, Unit.PERCENTAGE);
        box.addComponent(productSelect);
        // The layout shrinks to fit this label
        final Label label = new Label(getFieldTextLabel());
        label.addStyleName("ea-widthfittin-label");
        label.setWidthUndefined();
        label.setHeight("0px"); // Hide: Could be 0px
        box.addComponent(label);

        addValueChangeListener(e -> label.setValue(getFieldTextLabel()));

        return box;
    }

    private String getFieldTextLabel() {
        return getValue().getName();
    }
}
