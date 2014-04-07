/**
 *
 */
package ru.extas.web.users;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.security.UserRole;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Конвертирует роли пользователя в соответствующее перечисление
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToUserRoleConverter extends String2EnumConverter<UserRole> {

	/**
	 * <p>Constructor for StringToUserRoleConverter.</p>
	 */
	public StringToUserRoleConverter() {
		super(UserRole.class);
	}

	/** {@inheritDoc} */
	@Override
	protected BiMap<UserRole, String> createEnum2StringMap() {
		final BiMap<UserRole, String> map = HashBiMap.create();
		map.put(UserRole.USER, "Сотрудник");
		map.put(UserRole.MANAGER, "Руководитель");
		map.put(UserRole.ADMIN, "Администратор");
		map.put(UserRole.DEALER, "Дилер");
		map.put(UserRole.DEALER_MANAGER, "Дилер руководитель");
		map.put(UserRole.DISTRIBUTOR, "Дистрибьютор");
		return map;
	}
}
