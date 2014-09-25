package ru.extas.web.commons.component;

import com.vaadin.ui.OptionGroup;
import ru.extas.web.commons.ExtaTheme;

/**
 * @author Valery Orlov
 *         Date: 09.09.2014
 *         Time: 12:08
 */
public class YesNoSelect extends OptionGroup {

    public YesNoSelect(String caption) {
        super(caption);

        setMultiSelect(false);
        addStyleName(ExtaTheme.OPTIONGROUP_HORIZONTAL);
        setNullSelectionAllowed(false);
        setNewItemsAllowed(false);
        addItem(true); setItemCaption(true, "Да");
        addItem(false); setItemCaption(false, "Нет");
    }
}
