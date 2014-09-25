/**
 *
 */
package ru.extas.web.contacts;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.contacts.PeriodOfResidence;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * <p>StringToPersonPosition class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToPeriodOfResidence extends String2EnumConverter<PeriodOfResidence> {

	/**
	 * <p>Constructor for StringToPersonPosition.</p>
	 */
	public StringToPeriodOfResidence() {
		super(PeriodOfResidence.class);
	}

	/** {@inheritDoc} */
	@Override
	protected HashBiMap<PeriodOfResidence, String> createEnum2StringMap() {
        HashBiMap<PeriodOfResidence, String> map = HashBiMap.create();
		map.put(PeriodOfResidence.LESS_1, "До 1 года");
		map.put(PeriodOfResidence.FROM_1, "От 1 года");
		map.put(PeriodOfResidence.LESS_5, "До 5 лет");
		map.put(PeriodOfResidence.FROM_5, "Свыше 5 лет");
		return map;
	}
}
