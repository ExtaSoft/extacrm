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
 * @author Valery Orlov
 */
@Component
public class StringToLeadStatus extends String2EnumConverter<Lead.Status> {

	public StringToLeadStatus() {
		super(Lead.Status.class);
	}

	@Override
	protected BiMap<Lead.Status, String> createEnum2StringMap() {
		BiMap<Lead.Status, String> map = HashBiMap.create();
		map.put(Lead.Status.NEW, "Новый");
		map.put(Lead.Status.QUALIFIED, "Квалифицирован");
		map.put(Lead.Status.CLOSED, "Закрыт");
		return map;
	}
}
