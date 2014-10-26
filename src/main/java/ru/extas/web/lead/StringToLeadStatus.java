/**
 *
 */
package ru.extas.web.lead;

import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.lead.Lead;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * <p>StringToLeadStatus class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
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
	protected HashBiMap<Lead.Status, String> createEnum2StringMap() {
        final HashBiMap<Lead.Status, String> map = HashBiMap.create();
		map.put(Lead.Status.NEW, "Новый");
		map.put(Lead.Status.QUALIFIED, "Квалифицирован");
		map.put(Lead.Status.CLOSED, "Закрыт");
		return map;
	}
}
