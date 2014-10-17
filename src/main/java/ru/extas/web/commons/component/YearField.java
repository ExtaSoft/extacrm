package ru.extas.web.commons.component;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.PopupDateField;
import org.joda.time.LocalDate;

import java.util.Date;
import java.util.Locale;

/**
 * Селектор года
 * @author Valery Orlov
 *         Date: 11.09.2014
 *         Time: 13:28
 */
public class YearField extends PopupDateField {

    public YearField(final String caption) {
        setInputPrompt("Год");
        setResolution(Resolution.YEAR);
        setWidth(5, Unit.EM);
        setConverter(new Converter<Date, Integer>() {
            @Override
            public Integer convertToModel(final Date value, final Class<? extends Integer> targetType, final Locale locale) throws ConversionException {
                if (value != null) {
                    return new LocalDate(value).getYear();
                }
                return null;
            }

            @Override
            public Date convertToPresentation(final Integer value, final Class<? extends Date> targetType, final Locale locale) throws ConversionException {
                if (value != null) {
                    return new LocalDate(value, 1, 1).toDate();
                }
                return null;
            }

            @Override
            public Class<Integer> getModelType() {
                return Integer.class;
            }

            @Override
            public Class<Date> getPresentationType() {
                return Date.class;
            }
        });

    }
}
