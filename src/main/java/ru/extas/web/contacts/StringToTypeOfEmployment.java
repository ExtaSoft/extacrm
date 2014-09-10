/**
 *
 */
package ru.extas.web.contacts;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.contacts.EducationKind;
import ru.extas.model.contacts.TypeOfEmployment;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToTypeOfEmployment extends String2EnumConverter<TypeOfEmployment> {

    /**
     * <p>Constructor for StringToPersonPosition.</p>
     */
    public StringToTypeOfEmployment() {
        super(TypeOfEmployment.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BiMap<TypeOfEmployment, String> createEnum2StringMap() {
        BiMap<TypeOfEmployment, String> map = HashBiMap.create();
        map.put(TypeOfEmployment.UNEMPLOYED, "Не работает");
        map.put(TypeOfEmployment.PERMANENT, "Постоянная (без срока)");
        map.put(TypeOfEmployment.TEMPORARY, "Срочный трудовой договор");
        map.put(TypeOfEmployment.ENTREPRENEUR, "Индивиальный предприниматель");
        map.put(TypeOfEmployment.PRACTICE, "Частная практика");
        return map;
    }
}
