/**
 *
 */
package ru.extas.web.util;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractSelect;
import org.vaadin.data.collectioncontainer.CollectionContainer;
import org.vaadin.viritin.fields.CaptionGenerator;
import ru.extas.web.commons.component.ExtaTokenField;

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
     */
    public static <TEnum extends Enum<TEnum>> void fillSelectByEnum(final ExtaTokenField component, final Class<TEnum> cls) {
        final Converter<String, TEnum> converter = VaadinSession.getCurrent().getConverterFactory()
                .createConverter(String.class, cls);
        final EnumSet<TEnum> enums = EnumSet.allOf(cls);
        component.setContainerDataSource(CollectionContainer.fromBeans(enums));
        for (final TEnum en : enums) {
            component.setTokenCaption(en, converter.convertToPresentation(en, null, null));
        }
    }

    /**
     * Создает и возвращает генератор заголовков перечисления для EnumSelect.
     * Реализация генератора предпологает, что в системе зарегестрирован соответствующий конвертер перечисления в строку.
     *
     * @param enumClass класс перечисления
     * @param <TEnum> Тип перечисления
     * @return новый генератор заголовков
     */
    public static <TEnum extends Enum<TEnum>> CaptionGenerator<TEnum> getEnumCaptionGenerator(final Class<TEnum> enumClass) {
        return new CaptionGenerator<TEnum>() {
            private final Converter<String, TEnum> converter = VaadinSession.getCurrent().getConverterFactory()
                    .createConverter(String.class, enumClass);

            @Override
            public String getCaption(final TEnum option) {
                return converter.convertToPresentation(option, null, null);
            }
        };
    }
}
