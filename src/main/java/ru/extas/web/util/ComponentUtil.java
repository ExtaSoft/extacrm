/**
 * 
 */
package ru.extas.web.util;

import java.util.EnumSet;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractSelect;

/**
 * Разные утилиты для компонентов пользовательского интерфейса
 * 
 * @author Valery Orlov
 * 
 */
public class ComponentUtil {

	/**
	 * Заполняем поле выбора элементами перечисления
	 * 
	 * @param component
	 *            поле выбора
	 * @param cls
	 *            тип перечисления
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
}
