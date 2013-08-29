/**
 *
 */
package ru.extas.web.contacts;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vaadin.data.util.converter.Converter;
import ru.extas.model.PersonInfo;

import java.util.Locale;

/**
 * @author Valery Orlov
 */
public class StringToPersonPosition implements Converter<String, PersonInfo.Position> {

    private static final long serialVersionUID = 7270069509495749676L;

    private final BiMap<PersonInfo.Position, String> map;

    public StringToPersonPosition() {
        map = HashBiMap.create();
        map.put(PersonInfo.Position.EMPLOYEE, "Сотрудник");
        map.put(PersonInfo.Position.DIRECTOR, "Генеральный директор");
        map.put(PersonInfo.Position.ACCOUNTANT, "Главный бухгалтер");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getModelType()
     */
    @Override
    public Class<PersonInfo.Position> getModelType() {
        return PersonInfo.Position.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getPresentationType()
     */
    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
     * java.lang.Class, java.util.Locale)
     */
    @Override
    public PersonInfo.Position convertToModel(final String value, final Class<? extends PersonInfo.Position> targetType, final Locale locale)
            throws ConversionException {
        if (value == null || value.isEmpty())
            return null;
        return map.inverse().get(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
     * .Object, java.lang.Class, java.util.Locale)
     */
    @Override
    public String convertToPresentation(final PersonInfo.Position value, final Class<? extends String> targetType, final Locale locale)
            throws ConversionException {
        if (value == null)
            return null;
        return map.get(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
     * java.util.Locale)
     */
    public PersonInfo.Position convertToModel(final String value, final Locale locale)
            throws ConversionException {
        return convertToModel(value, null, locale);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
     * .Object, java.util.Locale)
     */
    public String convertToPresentation(final PersonInfo.Position value, final Locale locale)
            throws ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
