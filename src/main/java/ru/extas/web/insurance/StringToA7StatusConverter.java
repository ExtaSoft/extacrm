/**
 *
 */
package ru.extas.web.insurance;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.insurance.A7Form;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Конвертирует роли пользователя в соответствующее перечисление
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToA7StatusConverter extends String2EnumConverter<A7Form.Status> {

	/**
	 * <p>Constructor for StringToA7StatusConverter.</p>
	 */
	public StringToA7StatusConverter() {
		super(A7Form.Status.class);
	}

	/** {@inheritDoc} */
	@Override
	protected HashBiMap<A7Form.Status, String> createEnum2StringMap() {
        HashBiMap<A7Form.Status, String> map = HashBiMap.create();
		map.put(A7Form.Status.NEW, "Новый бланк");
		map.put(A7Form.Status.SPENT, "Использованный бланк");
		map.put(A7Form.Status.LOST, "Потерянный бланк");
		map.put(A7Form.Status.BROKEN, "Испорченный бланк");
		return map;
	}
}
