/**
 *
 */
package ru.extas.web.lead;

import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.lead.Lead;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Конвертируем результат завершения лида в текстовое представление
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToLeadResult extends String2EnumConverter<Lead.Result> {

	/**
	 * <p>Constructor for StringToLeadResult.</p>
	 */
	public StringToLeadResult() {
		super(Lead.Result.class);
	}

	/** {@inheritDoc} */
	@Override
	protected HashBiMap<Lead.Result, String> createEnum2StringMap() {
        final HashBiMap<Lead.Result, String> map = HashBiMap.create();
		map.put(Lead.Result.SUCCESSFUL, "Успешное выполнение");
		map.put(Lead.Result.CLIENT_REJECTED, "Отказ клиента");
		map.put(Lead.Result.VENDOR_REJECTED, "Отказ контрагента");
		map.put(Lead.Result.DOUBLE_REJECTED, "Закрыт как дублирующий");
		return map;
	}
}
