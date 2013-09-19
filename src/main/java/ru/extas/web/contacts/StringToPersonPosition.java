/**
 *
 */
package ru.extas.web.contacts;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vaadin.data.util.converter.Converter;
import ru.extas.model.Person;

import java.util.Locale;

/**
 * @author Valery Orlov
 */
public class StringToPersonPosition implements Converter<String, Person.Position> {

    private static final long serialVersionUID = 7270069509495749676L;

    private final BiMap<Person.Position, String> map;

    public StringToPersonPosition() {
        map = HashBiMap.create();
        map.put(Person.Position.EMPLOYEE, "Сотрудник");
        map.put(Person.Position.DIRECTOR, "Генеральный директор");
        map.put(Person.Position.ACCOUNTANT, "Главный бухгалтер");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getModelType()
     */
    @Override
    public Class<Person.Position> getModelType() {
        return Person.Position.class;
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
    public Person.Position convertToModel(final String value, final Class<? extends Person.Position> targetType, final Locale locale)
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
    public String convertToPresentation(final Person.Position value, final Class<? extends String> targetType, final Locale locale)
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
    public Person.Position convertToModel(final String value, final Locale locale)
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
    public String convertToPresentation(final Person.Position value, final Locale locale)
            throws ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
