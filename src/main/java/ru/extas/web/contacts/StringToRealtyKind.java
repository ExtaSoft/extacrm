/**
 *
 */
package ru.extas.web.contacts;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.contacts.RealtyKind;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * <p>StringToPersonPosition class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToRealtyKind extends String2EnumConverter<RealtyKind> {

	/**
	 * <p>Constructor for StringToPersonPosition.</p>
	 */
	public StringToRealtyKind() {
		super(RealtyKind.class);
	}

	/** {@inheritDoc} */
	@Override
	protected HashBiMap<RealtyKind, String> createEnum2StringMap() {
        HashBiMap<RealtyKind, String> map = HashBiMap.create();
		map.put(RealtyKind.PROPERTY, "Собственность");
		map.put(RealtyKind.PART_PROPERTY, "Долевая собственность");
		map.put(RealtyKind.SOCIAL, "Социальный найм");
		map.put(RealtyKind.RENT, "Аренда");
		map.put(RealtyKind.RELATIONALS, "У родственников");
		return map;
	}
}
