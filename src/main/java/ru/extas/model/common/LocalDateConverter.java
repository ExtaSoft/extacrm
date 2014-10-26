package ru.extas.model.common;


import org.joda.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;

/**
 * <p>LocalDateConverter class.</p>
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.3
 */
@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

    /** {@inheritDoc} */
    @Override
    public Date convertToDatabaseColumn(final LocalDate attribute) {
        Date dbData = null;
        if (attribute != null)
            dbData = new Date(attribute.toDate().getTime());
        return dbData;
    }

    /** {@inheritDoc} */
    @Override
    public LocalDate convertToEntityAttribute(final Date dbData) {
        LocalDate attribute = null;
        if (dbData != null)
            attribute = new LocalDate(dbData.getTime());
        return attribute;
    }
}
