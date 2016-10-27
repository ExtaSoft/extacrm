package ru.extas.web.contacts.legalentity;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.server.references.SupplementService;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Created by valery on 23.09.16.
 */
public class TaxTypeSelect extends ComboBox {

    public TaxTypeSelect() {
        this("Форма налогообложения");
    }

    public TaxTypeSelect(final String caption) {
        super(caption);
        setDescription("Выберите форму налогооблажения ЮЛ");
        setInputPrompt("Выберите...");
        setImmediate(true);
        setScrollToSelectedItem(true);
        setNullSelectionAllowed(false);
        setNewItemsAllowed(false);
        setFilteringMode(FilteringMode.CONTAINS);
        setWidth(20, Unit.EM);
        lookup(SupplementService.class).loadTaxTypes().forEach((final String item) -> addItem(item));
    }
}
