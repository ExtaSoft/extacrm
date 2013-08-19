package ru.extas.web.commons;

import com.vaadin.data.util.converter.Converter;

import java.util.EnumSet;

/**
 * Привязка пользовательского интерфейса с данным
 *
 * @author Valery Orlov
 */
public class DataDeclMapping {

    private final String propName;
    private final String caption;
    private final EnumSet<PresentFlag> presentFlags;
    private final Converter<String, ?> converter;

    /**
     * Параметры отображения
     *
     * @author Valery Orlov
     */
    public enum PresentFlag {
        /**
         * Свернутый столбец в гриде
         */
        COLLAPSED
    }

    public DataDeclMapping(String propName, String caption) {
        this(propName, caption, null, null);
    }

    public DataDeclMapping(String propName, String caption, EnumSet<PresentFlag> presentFlags) {
        this(propName, caption, presentFlags, null);
    }

    public DataDeclMapping(String propName, String caption, Converter<String, ?> converter) {
        this(propName, caption, null, converter);
    }

    public DataDeclMapping(String propName, String caption, EnumSet<PresentFlag> presentFlags, Converter<String, ?> converter) {
        super();
        this.propName = propName;
        this.caption = caption;
        if (presentFlags == null)
            this.presentFlags = EnumSet.noneOf(PresentFlag.class);
        else
            this.presentFlags = presentFlags;
        this.converter = converter;
    }

    /**
     * @return the propName
     */
    public final String getPropName() {
        return propName;
    }

    /**
     * @return the caption
     */
    public final String getCaption() {
        return caption;
    }

    /**
     * @return the collapsed
     */
    public final boolean isCollapsed() {
        return presentFlags.contains(PresentFlag.COLLAPSED);
    }

    /**
     * @return the converter
     */
    public Converter<String, ?> getConverter() {
        return converter;
    }

}