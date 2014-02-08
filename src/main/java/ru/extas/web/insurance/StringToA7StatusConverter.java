/**
 *
 */
package ru.extas.web.insurance;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.A7Form;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Конвертирует роли пользователя в соответствующее перечисление
 *
 * @author Valery Orlov
 */
@Component
public class StringToA7StatusConverter extends String2EnumConverter<A7Form.Status> {

	public StringToA7StatusConverter() {
		super(A7Form.Status.class);
	}

	@Override
	protected BiMap<A7Form.Status, String> createEnum2StringMap() {
		BiMap<A7Form.Status, String> map = HashBiMap.create();
		map.put(A7Form.Status.NEW, "Новый бланк");
		map.put(A7Form.Status.SPENT, "Использованный бланк");
		map.put(A7Form.Status.LOST, "Потерянный бланк");
		map.put(A7Form.Status.BROKEN, "Испорченный бланк");
		return map;
	}
}
