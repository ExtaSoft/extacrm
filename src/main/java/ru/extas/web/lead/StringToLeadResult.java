/**
 *
 */
package ru.extas.web.lead;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.Lead;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Конвертируем результат завершения лида в текстовое представление
 *
 * @author Valery Orlov
 */
@Component
public class StringToLeadResult extends String2EnumConverter<Lead.Result> {

	public StringToLeadResult() {
		super(Lead.Result.class);
	}

	@Override
	protected BiMap<Lead.Result, String> createEnum2StringMap() {
		BiMap<Lead.Result, String> map = HashBiMap.create();
		map.put(Lead.Result.SUCCESSFUL, "Успешное выполнение");
		map.put(Lead.Result.CLIENT_REJECTED, "Отказ клиента");
		map.put(Lead.Result.VENDOR_REJECTED, "Отказ контрагента");
		return map;
	}
}
