/**
 *
 */
package ru.extas.web.product;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import ru.extas.model.sale.Product;
import ru.extas.server.sale.ProductRepository;
import ru.extas.web.commons.ExtaBeanContainer;
import ru.extas.web.commons.ExtaTheme;

import java.util.List;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Компонент выбора продукта
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class ProductSelect extends CustomField<Product> {

	private static final long serialVersionUID = 6004206917183679455L;
	private final Product product;

	/** {@inheritDoc} */
	@Override
	public Class<Product> getType() {
		return Product.class;
	}

	/**
	 * <p>Constructor for ProductSelect.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 * @param product a {@link ru.extas.model.sale.Product} object.
	 */
	public ProductSelect(final String caption, final String description, final Product product) {
		this.product = product;
		setCaption(caption);
		setDescription(description);
	}

	@Override
	protected Component initContent() {
		// A vertical layout with undefined width
		VerticalLayout box = new VerticalLayout();
		box.setSizeUndefined();

		ComboBox productSelect = new ComboBox();
		productSelect.setInputPrompt("Выберите продукт...");
		productSelect.setImmediate(true);
		productSelect.setNullSelectionAllowed(false);

		// Инициализация контейнера
		final ProductRepository productRepository = lookup(ProductRepository.class);
		final List<Product> products = productRepository.findByActiveOrderByNameAsc(true);
		final ExtaBeanContainer<Product> clientsCont = new ExtaBeanContainer<>(Product.class);
		clientsCont.addAll(products);
		if (product != null) {
			final Product forceProduct = productRepository.findOne(product.getId());
			clientsCont.addBean(forceProduct);
		}

		// Устанавливаем контент выбора
		productSelect.setFilteringMode(FilteringMode.CONTAINS);
		productSelect.setContainerDataSource(clientsCont);
		productSelect.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		productSelect.setItemCaptionPropertyId("name");
		productSelect.addStyleName(ExtaTheme.COMBOBOX_BORDERLESS);

		productSelect.addValueChangeListener(e -> setValue((Product) productSelect.getValue()));
		productSelect.setValue(getValue());
		productSelect.setWidth(100, Unit.PERCENTAGE);
		box.addComponent(productSelect);
		// The layout shrinks to fit this label
		Label label = new Label(getFieldTextLabel());
//		label.addStyleName("monospace");
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
