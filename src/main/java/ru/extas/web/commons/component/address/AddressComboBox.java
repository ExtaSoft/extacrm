package ru.extas.web.commons.component.address;

import com.vaadin.data.Container;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;

/**
 * Комбобокс для подсказок адреса.
 *
 * @author sandarkin
 *
 * @since 2.0
 * @version 2.0
 */
public class AddressComboBox extends ComboBox {

    public AddressComboBox() {
        super(null);
    }

    public AddressComboBox(final String caption) {
        this.setCaption(caption);

        // the item caption mode has to be PROPERTY for the filtering to work
        setItemCaptionMode(ItemCaptionMode.PROPERTY);

        // define the property name of the CountryBean to use as item caption
        setItemCaptionPropertyId("value");
    }



    @Override
    protected Filter buildFilter(String filterString, FilteringMode filteringMode) {
        return new AddressSuggestingContainer.SuggestionFilter(filterString);
    }
}
