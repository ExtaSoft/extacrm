/**
 *
 */
package ru.extas.web.contacts.person;

import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.contacts.MaritalStatus;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * <p>StringToPersonPosition class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToMaritalStatus extends String2EnumConverter<MaritalStatus> {

	/**
	 * <p>Constructor for StringToPersonPosition.</p>
	 */
	public StringToMaritalStatus() {
		super(MaritalStatus.class);
	}

	/** {@inheritDoc} */
	@Override
	protected HashBiMap<MaritalStatus, String> createEnum2StringMap() {
        final HashBiMap<MaritalStatus, String> map = HashBiMap.create();
		map.put(MaritalStatus.CIVIL_MARRIAGE, "Гражданский брак");
		map.put(MaritalStatus.SINGLE, "Холост/не замужем");
		map.put(MaritalStatus.MARRIAGE, "Женат/замужем");
		map.put(MaritalStatus.DIVORCED, "Разведен(а)");
		map.put(MaritalStatus.WIDOWED, "Вдовец/вдова");
		return map;
	}
}
