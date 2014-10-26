/**
 *
 */
package ru.extas.web.users;

import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.security.SecureAction;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Конвертирует действия в соответствующее перечисление
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToSecureActionConverter extends String2EnumConverter<SecureAction> {

    /**
     * <p>Constructor for StringToSecureActionConverter.</p>
     */
    public StringToSecureActionConverter() {
        super(SecureAction.class);
    }

    /** {@inheritDoc} */
    @Override
    protected HashBiMap<SecureAction, String> createEnum2StringMap() {
        final HashBiMap<SecureAction, String> map = HashBiMap.create();
        map.put(SecureAction.VIEW, "Чтение/просмотр");
        map.put(SecureAction.EDIT, "Редактирование");
        map.put(SecureAction.INSERT, "Ввод");
        map.put(SecureAction.DELETE, "Удаление");
        map.put(SecureAction.ALL, "Полный доступ");
        return map;
    }
}
