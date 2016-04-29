package ru.extas.web.commons.component;

import com.vaadin.ui.ComboBox;
import org.vaadin.viritin.fields.TypedSelect;

/**
 * Типизированый компонент выбора
 *
 * Created by valery on 28.04.16.
 */
public class TypedComboBox<T> extends TypedSelect<T> {

    /**
     * The type of element options in the select
     *
     * @param type the type of options in the list
     */
    public TypedComboBox(Class<T> type) {
        super(type);
        initialize();
    }

    private void initialize() {
        super.withSelectType(ComboBox.class);
    }

    /**
     * Note, that with this constructor, you cannot override the select type.
     *
     * @param options options to select from
     */
    public TypedComboBox(T... options) {
        super(options);
        initialize();
    }

    public TypedComboBox(String caption) {
        super(caption);
        initialize();
    }

    public TypedComboBox<T> setNewItemsAllowed(boolean newItemsAllowed) {
        getSelect().setNewItemsAllowed(newItemsAllowed);
        return this;
    }

    public boolean isNewItemsAllowed() {
        return getSelect().isNewItemsAllowed();
    }
}
