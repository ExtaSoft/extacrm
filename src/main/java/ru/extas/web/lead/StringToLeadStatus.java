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
 * <p>StringToLeadStatus class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
@Component
public class StringToLeadStatus extends String2EnumConverter<Lead.Status> {

	/**
	 * <p>Constructor for StringToLeadStatus.</p>
	 */
	public StringToLeadStatus() {
		super(Lead.Status.class);
	}

	/** {@inheritDoc} */
	@Override
	protected BiMap<Lead.Status, String> createEnum2StringMap() {
		BiMap<Lead.Status, String> map = HashBiMap.create();
		map.put(Lead.Status.NEW, "Новый");
		map.put(Lead.Status.QUALIFIED, "Квалифицирован");
		map.put(Lead.Status.CLOSED, "Закрыт");
		return map;
	}
}
