package ru.extas.model.common;

import org.joda.time.DateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;

/**
 * <p>DateTimeConverter class.</p>
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.3
 */
@Converter(autoApply = true)
public class DateTimeConverter implements AttributeConverter<DateTime, Timestamp> {

    /** {@inheritDoc} */
    @Override
    public Timestamp convertToDatabaseColumn(final DateTime attribute) {
        Timestamp dbVal = null;
        if (attribute != null) {
            dbVal = new Timestamp(attribute.getMillis());
        }
        return dbVal;
    }

    /** {@inheritDoc} */
    @Override
    public DateTime convertToEntityAttribute(final Timestamp dbData) {
        DateTime attribute = null;
        if (dbData != null)
            attribute = new DateTime(dbData.getTime());
        return attribute;
    }
}
