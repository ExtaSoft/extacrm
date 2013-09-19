/**
 *
 */
package ru.extas.web.contacts;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.model.Contact;
import ru.extas.web.commons.ExtaDataContainer;

/**
 * Абстрактный компонент для выбора контакта
 *
 * @author Valery Orlov
 */
public abstract class AbstractContactSelect<TContactType extends Contact> extends ComboBox {

    private static final long serialVersionUID = -8005905898383483037L;
    private final Class<TContactType> contactType;
    protected JPAContainer<TContactType> container;

    protected AbstractContactSelect(final String caption, final Class<TContactType> contactType) {
        this(caption, "Выберете существующий контакт или введите новый", contactType);
    }

    protected AbstractContactSelect(final String caption, final String description, final Class<TContactType> contactType) {
        super(caption);

        this.contactType = contactType;

        // Преконфигурация
        setDescription(description);
        setInputPrompt("Существующий или новый контакт");
        setWidth(25, Unit.EM);
        setImmediate(true);

        // Инициализация контейнера
        container = new ExtaDataContainer<>(contactType);

        // Устанавливаем контент выбора
        setFilteringMode(FilteringMode.CONTAINS);
        setContainerDataSource(container);
        setItemCaptionMode(ItemCaptionMode.PROPERTY);
        setItemCaptionPropertyId("name");
        setConverter(new SingleSelectConverter<TContactType>(this));

        // Функционал добавления нового контакта
        setNullSelectionAllowed(false);
        setNewItemsAllowed(false);
    }

    /**
     * Returns the type of the property. <code>getValue</code> and
     * <code>setValue</code> methods must be compatible with this type: one can
     * safely cast <code>getValue</code> to given type and pass any variable
     * assignable to this type as a parameter to <code>setValue</code>.
     *
     * @return the Type of the property.
     */
//    @Override
//    public Class<?> getType() {
//        return contactType;
//    }

}
