/**
 *
 */
package ru.extas.web.contacts;

import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.model.contacts.Contact;
import ru.extas.web.commons.ExtaDataContainer;

/**
 * Абстрактный компонент для выбора контакта
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class AbstractContactSelect<TContactType extends Contact> extends ComboBox {

    private static final long serialVersionUID = -8005905898383483037L;
    protected ExtaDataContainer<TContactType> container;

    /**
     * <p>Constructor for AbstractContactSelect.</p>
     *
     * @param caption     a {@link java.lang.String} object.
     * @param contactType a {@link java.lang.Class} object.
     */
    protected AbstractContactSelect(final String caption, final Class<TContactType> contactType) {
        this(caption, "Выберите существующий контакт или введите новый", contactType, null);
    }


    /**
     *
     * @param caption
     * @param description
     * @param contactType
     */
    protected AbstractContactSelect(final String caption, final String description, final Class<TContactType> contactType) {
        this(caption, description, contactType, null);
    }

    /**
     * <p>Constructor for AbstractContactSelect.</p>
     *
     * @param caption     a {@link String} object.
     * @param description a {@link String} object.
     * @param contactType a {@link Class} object.
     * @param filter
     */
    protected AbstractContactSelect(final String caption, final String description, final Class<TContactType> contactType, Filter filter) {
        super(caption);

        Class<TContactType> contactType1 = contactType;

        // Преконфигурация
        setDescription(description);
        setInputPrompt("контакт...");
        setWidth(25, Unit.EM);
        setImmediate(true);

        // Инициализация контейнера
        container = new ExtaDataContainer<>(contactType);
        if (filter != null) {
            container.addContainerFilter(filter);
        }

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
     * <p>refreshContainer.</p>
     */
    public void refreshContainer() {
        container.refresh();
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
