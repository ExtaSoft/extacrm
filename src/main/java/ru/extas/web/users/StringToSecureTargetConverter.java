/**
 *
 */
package ru.extas.web.users;

import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.security.SecureTarget;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Конвертирует целевые объекты в соответствующее перечисление
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToSecureTargetConverter extends String2EnumConverter<SecureTarget> {

	/**
	 * <p>Constructor for StringToUserRoleConverter.</p>
	 */
	public StringToSecureTargetConverter() {
		super(SecureTarget.class);
	}

	/** {@inheritDoc} */
	@Override
	protected HashBiMap<SecureTarget, String> createEnum2StringMap() {
		final HashBiMap<SecureTarget, String> map = HashBiMap.create();
        map.put(SecureTarget.OWNONLY, "Собственные объекты");
        map.put(SecureTarget.SALE_POINT, "Объекты той же торговой точки");
        map.put(SecureTarget.CORPORATE, "Корпоративные объекты (той же компании)");
        map.put(SecureTarget.ALL, "Все объекты");
        return map;
	}
}
