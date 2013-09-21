package ru.extas.model.converter;

import org.joda.time.DateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;

@Converter(autoApply = true)
public class DateTimeConverter implements AttributeConverter<DateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(final DateTime attribute) {
        Timestamp dbVal = null;
        if (attribute != null) {
            dbVal = new Timestamp(attribute.getMillis());
        }
        return dbVal;
    }

    @Override
    public DateTime convertToEntityAttribute(final Timestamp dbData) {
        DateTime attribute = null;
        if (dbData != null)
            attribute = new DateTime(dbData.getTime());
        return attribute;
    }
}