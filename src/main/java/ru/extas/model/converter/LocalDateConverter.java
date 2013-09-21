package ru.extas.model.converter;


import org.joda.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(final LocalDate attribute) {
        Date dbData = null;
        if (attribute != null)
            dbData = new Date(attribute.toDate().getTime());
        return dbData;
    }

    @Override
    public LocalDate convertToEntityAttribute(final Date dbData) {
        LocalDate attribute = null;
        if (dbData != null)
            attribute = new LocalDate(dbData.getTime());
        return attribute;
    }
}