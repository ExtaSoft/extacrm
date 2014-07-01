/**
 *
 */
package ru.extas.web.util;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractSelect;
import org.vaadin.data.collectioncontainer.CollectionContainer;
import org.vaadin.tokenfield.TokenField;

import java.util.EnumSet;

/**
 * Разные утилиты для компонентов пользовательского интерфейса
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class ComponentUtil {

    /**
     * Заполняем поле выбора элементами перечисления
     *
     * @param component поле выбора
     * @param cls       тип перечисления
     * @param <TEnum> a TEnum object.
     */
    public static <TEnum extends Enum<TEnum>> void fillSelectByEnum(final AbstractSelect component,
                                                                    final Class<TEnum> cls) {
        final Converter<String, TEnum> converter = VaadinSession.getCurrent().getConverterFactory()
                .createConverter(String.class, cls);
        for (final TEnum en : EnumSet.allOf(cls)) {
            component.addItem(en);
            component.setItemCaption(en, converter.convertToPresentation(en, null, null));
        }
    }

    /**
     * Заполняем поле выбора элементами перечисления
     *
     * @param component поле выбора
     * @param cls       тип перечисления
     * @param <TEnum> a TEnum object.
     */
    public static <TEnum extends Enum<TEnum>> void fillSelectByEnum(final TokenField component, final Class<TEnum> cls) {
        final Converter<String, TEnum> converter = VaadinSession.getCurrent().getConverterFactory()
                .createConverter(String.class, cls);
        EnumSet<TEnum> enums = EnumSet.allOf(cls);
        component.setContainerDataSource(CollectionContainer.fromBeans(enums));
        for (final TEnum en : enums) {
            component.setTokenCaption(en, converter.convertToPresentation(en, null, null));
        }
    }
}
