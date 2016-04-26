package ru.extas.web.commons;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ConverterUtil;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.datefield.Resolution;
import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

import java.util.Locale;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * Позволяет настроить параметры отображения фильтра таблицы
 * <p>
 * Created by valery on 26.04.16.
 */
public class CommonFilterDecorator implements FilterDecorator {

    private Map<Class, Converter> converterCache = newHashMap();

    /**
     * Returns the filter display name for the given enum value when filtering
     * the given property id.
     *
     * @param propertyId ID of the property the filter is attached to.
     * @param value      Value of the enum the display name is requested for.
     * @return UI Display name for the enum value.
     */
    @Override
    public String getEnumFilterDisplayName(Object propertyId, Object value) {
        final Class<?> type = value.getClass();
        Converter converter = converterCache.get(type);
        if (converter == null) {
            converter = ConverterUtil.getConverter(String.class, type, null);
            if (converter != null)
                converterCache.put(type, converter);
        }
        if (converter != null)
            return (String) converter.convertToPresentation(value, String.class, null);

        return null;
    }

    /**
     * Returns the filter icon for the given enum value when filtering the given
     * property id.
     *
     * @param propertyId ID of the property the filter is attached to.
     * @param value      Value of the enum the icon is requested for.
     * @return Resource for the icon of the enum value.
     */
    @Override
    public Resource getEnumFilterIcon(Object propertyId, Object value) {
        return null;
    }

    /**
     * Returns the filter display name for the given boolean value when
     * filtering the given property id.
     *
     * @param propertyId ID of the property the filter is attached to.
     * @param value      Value of boolean the display name is requested for.
     * @return UI Display name for the given boolean value.
     */
    @Override
    public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
        return value ? "Да" : "Нет";
    }

    /**
     * Returns the filter icon for the given boolean value when filtering the
     * given property id.
     *
     * @param propertyId ID of the property the filter is attached to.
     * @param value      Value of boolean the icon is requested for.
     * @return Resource for the icon of the given boolean value.
     */
    @Override
    public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
        return null;
    }

    /**
     * Returns whether the text filter should update as the user types. This
     * uses {@link TextChangeEventMode#LAZY}
     *
     * @param propertyId
     * @return true if the text field should use a TextChangeListener.
     */
    @Override
    public boolean isTextFilterImmediate(Object propertyId) {
        return false;
    }

    /**
     * The text change timeout dictates how often text change events are
     * communicated to the application, and thus how often are the filter values
     * updated.
     *
     * @param propertyId
     * @return the timeout in milliseconds
     */
    @Override
    public int getTextChangeTimeout(Object propertyId) {
        return 0;
    }

    /**
     * Return display caption for the From field
     *
     * @return caption for From field
     */
    @Override
    public String getFromCaption() {
        return null;
    }

    /**
     * Return display caption for the To field
     *
     * @return caption for To field
     */
    @Override
    public String getToCaption() {
        return null;
    }

    /**
     * Return display caption for the Set button
     *
     * @return caption for Set button
     */
    @Override
    public String getSetCaption() {
        return null;
    }

    /**
     * Return display caption for the Clear button
     *
     * @return caption for Clear button
     */
    @Override
    public String getClearCaption() {
        return null;
    }

    /**
     * Return DateField resolution for the Date filtering of the property ID.
     * This will only be called for Date -typed properties. Filtering values
     * output by the FilteringTable will also be truncated to this resolution.
     *
     * @param propertyId ID of the property the resolution will be applied to
     * @return A resolution defined in {@link DateField}
     */
    @Override
    public Resolution getDateFieldResolution(Object propertyId) {
        return null;
    }

    /**
     * Returns a date format pattern to be used for formatting the date/time
     * values shown in the filtering field of the given property ID. Note that
     * this is completely independent from the resolution set for the property,
     * and is used for display purposes only.
     * <p>
     * See SimpleDateFormat for the pattern definition
     *
     * @param propertyId ID of the property the format will be applied to
     * @return A date format pattern or null to use the default formatting
     */
    @Override
    public String getDateFormatPattern(Object propertyId) {
        return null;
    }

    /**
     * Returns the locale to be used with Date filters. If none is provided,
     * reverts to default locale of the system.
     *
     * @return Desired locale for the dates
     */
    @Override
    public Locale getLocale() {
        return null;
    }

    /**
     * Return the string that should be used as an "input prompt" when no
     * filtering is made on a filter component.
     *
     * @return String to show for no filter defined
     */
    @Override
    public String getAllItemsVisibleString() {
        return null;
    }

    /**
     * Return configuration for the numeric filter field popup
     *
     * @return Configuration for numeric filter
     */
    @Override
    public NumberFilterPopupConfig getNumberFilterPopupConfig() {
        return null;
    }

    /**
     * Defines whether a popup-style numeric filter should be used for the
     * property with the given ID.
     * <p>
     * The types Integer, Long, Float and Double are considered to be 'numeric'
     * within this context.
     *
     * @param propertyId ID of the property the popup will be applied to
     * @return true to use popup-style, false to use a TextField
     */
    @Override
    public boolean usePopupForNumericProperty(Object propertyId) {
        return false;
    }
}
